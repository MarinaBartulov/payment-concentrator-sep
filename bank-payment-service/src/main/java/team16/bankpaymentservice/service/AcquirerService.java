package team16.bankpaymentservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import team16.bankpaymentservice.config.EndpointConfig;
import team16.bankpaymentservice.config.RestConfig;
import team16.bankpaymentservice.dto.*;
import team16.bankpaymentservice.enums.TransactionStatus;
import team16.bankpaymentservice.exceptions.InappropriateBankException;
import team16.bankpaymentservice.exceptions.InvalidDataException;
import team16.bankpaymentservice.exceptions.LackingFundsException;
import team16.bankpaymentservice.model.*;
import team16.bankpaymentservice.service.impl.CardServiceImpl;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

@Service
public class AcquirerService {

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CardOwnerService cardOwnerService;

    @Autowired
    private CardService cardService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestConfig configuration;

    private ValidationService validationService;

    Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);

    public AcquirerService() { validationService = new ValidationService(); }

    // validirati DTO
    // porveriti na osnovu PAN-a Merchant-a vezanog za taj Payment/Transaction
    // i PAN-a unetog na formi od strane Client-a,
    // da li su oni u istoj banci
    // ako da
    // validirati podatke i izvrsiti placanje
    // ako ne
    // onda se salje zahtev za placanje na pcc
    public TransactionResponseDTO initialPayment(ClientAuthDTO dto, Long paymentId) throws Exception {

        if(paymentService.findById(paymentId) == null) {
            logger.error("Nonexistent payment");
            throw new Exception("Nonexistent payment.");
        }

        Payment payment = paymentService.findById(paymentId);
        Transaction transaction = payment.getTransaction();
        Merchant merchant = transaction.getMerchant();
        Card merchantCard = merchant.getCard();

        TransactionResponseDTO responseDTO = new TransactionResponseDTO();

        // u zavisnosti od problema baciti odgovarajuci exception i odraditi odgovarajuce redirection
        try {
            validateClientInput(dto, merchantCard.getPAN());
        } catch (InvalidDataException ide) {
            //responseDTO.setRedirectionURL(merchant.getMerchantErrorUrl());
            // POZOVI FINISH PAYMENT
            responseDTO.setResponseMessage(ide.getMessage());
            transaction.setStatus(TransactionStatus.FAILED);
            Transaction t1 = transactionService.update(transaction);
            responseDTO.setTransactionStatus(t1.getStatus().toString());
            return responseDTO;
        } catch(InappropriateBankException ibe) {

            logger.info("Redirection to PCC");

            // generise se ACQUIRER_ORDER_ID i ACQUIRER_TIMESTAMP
            // ACQUIRER_ORDER_ID je id Transaction-a dobijenog na osnovu Payment-a
            // ACQUIRER_TIMESTAMP je trenutno vreme

            transaction.setAcquirerOrderId(transaction.getId());
            transaction.setAcquirerTimestamp(LocalDateTime.now());

            Transaction t1 = transactionService.update(transaction);

            // zajedno sa podacima o kartici - podaci uneti sa fronta za autentifikaciju klijenta i kartice
            PCCRequestDTO pccRequestDTO = new PCCRequestDTO(dto.getPan(), dto.getSecurityNumber(),
                    dto.getCardHolderName(), dto.getExpirationDate(), t1.getMerchantOrderId(),
                    t1.getMerchantTimestamp(), payment.getPaymentId(), null, t1.getAcquirerOrderId(),
                    t1.getAcquirerTimestamp(), merchantCard.getPAN());

            // zahtev se salje na PCC
            HttpEntity<PCCRequestDTO> request = new HttpEntity<>(pccRequestDTO);
            ResponseEntity<PCCResponseDTO> response = null;

            try {
                logger.info("Sending request to PCC service");
                response = restTemplate.exchange(
                        getEndpoint(), HttpMethod.POST, request, PCCResponseDTO.class);
                logger.info("Received response from PCC service");

                //responseDTO.setResponseMessage("Transaction completed with status " + response.getBody().getStatus().toString());
                responseDTO.setTransactionStatus(response.getBody().getStatus().toString());

                if(response.getBody().getStatus().toString().equals("COMPLETED")) {
                    //responseDTO.setRedirectionURL(merchant.getMerchantSuccessUrl());
                    // POZOVI FINISH PAYMENT
                } else if(response.getBody().getStatus().toString().equals("FAILED")) {
                    //responseDTO.setRedirectionURL(merchant.getMerchantFailedUrl());
                    // POZOVI FINISH PAYMENT
                } else {
                    //responseDTO.setRedirectionURL(merchant.getMerchantErrorUrl());
                    // POZOVI FINISH PAYMENT
                }

                String PSPResponseMessage = finishPayment(response.getBody());
                responseDTO.setResponseMessage(PSPResponseMessage);

                return responseDTO;
            } catch (RestClientException e) {
                logger.error("RestTemplate error");
                //responseDTO.setRedirectionURL(merchant.getMerchantErrorUrl());
                // POZOVI FINISH PAYMENT
                responseDTO.setResponseMessage(e.getMessage());
                transaction.setStatus(TransactionStatus.FAILED);
                t1 = transactionService.update(transaction);
                responseDTO.setTransactionStatus(t1.getStatus().toString());
                return responseDTO;
            }

        } catch (Exception e) {
            //responseDTO.setRedirectionURL(merchant.getMerchantErrorUrl());
            // POZOVI FINISH PAYMENT
            transaction.setStatus(TransactionStatus.FAILED);
            Transaction t1 = transactionService.update(transaction);
            responseDTO.setTransactionStatus(t1.getStatus().toString());
            responseDTO.setResponseMessage(e.getMessage());
            return responseDTO;
        }

        // vezati transakciju sa klijentom
        Card clientCard = cardService.findByPan(dto.getPan());

        // provera sredstava sa racuna klijenta
        try {
            checkClientFunds(clientCard, transaction);
            logger.info("Enough available funds");
        } catch (LackingFundsException lfe) {
            //responseDTO.setRedirectionURL(merchant.getMerchantFailedUrl());
            // POZOVI FINISH PAYMENT
            transaction.setStatus(TransactionStatus.FAILED);
            Transaction t1 = transactionService.update(transaction);
            responseDTO.setTransactionStatus(t1.getStatus().toString());
            responseDTO.setResponseMessage(lfe.getMessage());
            return responseDTO;
        }

        // update sredstava sa kartica sa proverom sredstava klijenta
        clientCard.setAvailableFunds(clientCard.getAvailableFunds() - transaction.getAmount());
        merchantCard.setAvailableFunds(merchantCard.getAvailableFunds() + transaction.getAmount());
        transaction.setStatus(TransactionStatus.COMPLETED);

        cardService.update(clientCard);
        cardService.update(merchantCard);

        Client client = cardOwnerService.findClientByCardId(clientCard.getId());
        transaction.setClient(client);
        Transaction newT = transactionService.update(transaction);

        //responseDTO.setRedirectionURL(merchant.getMerchantSuccessUrl());
        responseDTO.setTransactionStatus(transaction.getStatus().toString());

        String PSPResponseMessage = finishPaymentOneBank(newT, payment.getPaymentId());
        responseDTO.setResponseMessage(PSPResponseMessage);

        logger.info("Transaction with only one Bank completed successfully");
        return responseDTO;
    }

    private void checkClientFunds(Card clientCard, Transaction transaction) throws LackingFundsException {
        if(clientCard.getAvailableFunds() < transaction.getAmount()) {
            logger.error("Not enough available funds");
            throw new LackingFundsException("Lacking funds.");
        } else {
            transaction.setStatus(TransactionStatus.CREATED);
            transactionService.update(transaction);
        }
    }

    private void validateClientInput(ClientAuthDTO dto, String merchantCardPAN) throws Exception {
        if(!validationService.validateString(dto.getPan()) ||
                !validationService.validateString(dto.getSecurityNumber()) ||
                !validationService.validateString(dto.getCardHolderName()) ||
                !validationService.validateString(dto.getCardHolderName())) {
            logger.error("ERROR | Client information empty");
            throw new InvalidDataException("Client information empty");
        }
        if(!dto.getPan().substring(0, 3).equals(merchantCardPAN.substring(0, 3))) {
            logger.error("Client and Merchant don't have an account in the same bank");
            throw new InappropriateBankException("Client and Merchant don't have an account in the same bank");
        }
        if(cardService.findByPan(dto.getPan()) == null) {
            logger.error("Invalid client information - Non existent client card pan");
            throw new InvalidDataException("Non existent client card pan");
        }

        Card card = cardService.findByPan(dto.getPan());

        if(!card.getSecurityCode().equals(dto.getSecurityNumber())) {
            logger.error("Invalid client information - security codes don't match");
            throw new InvalidDataException("Security codes don't match");
        }

        Client client = cardOwnerService.findClientByCardId(card.getId());
        if(!client.getFirstName().equals(dto.getCardHolderName())) {
            logger.error("Invalid client information - Card Holder Name not valid");
            throw new InvalidDataException("Card Holder Name not valid");
        }
        if(!validationService.convertToYearMonthFormat(dto.getExpirationDate()).equals(card.getExpirationDate())) {
            logger.error("Invalid client information - false expiration date");
            throw new InvalidDataException("False expiration date");
        }
    }

    private URI getEndpoint() throws URISyntaxException {
        return new URI(configuration.url() + EndpointConfig.PCC_SERVICE_BASE_URL + "/api/redirect");
    }

    private String finishPaymentOneBank(Transaction t, Long paymentId) {
        TransactionDTO responseDTO = new TransactionDTO();
        responseDTO.setPaymentId(paymentId);
        responseDTO.setMerchantOrderId(t.getMerchantOrderId());
        responseDTO.setAcquirerOrderId(t.getAcquirerOrderId());
        responseDTO.setAcquirerTimestamp(t.getAcquirerTimestamp());
        responseDTO.setIssuerOrderId(t.getIssuerOrderId());
        responseDTO.setIssuerTimestamp(t.getIssuerTimestamp());
        responseDTO.setStatus(t.getStatus().toString());

        // zahtev se salje na PSP
        HttpEntity<TransactionDTO> request = new HttpEntity<>(responseDTO);
        ResponseEntity<String> response = null;

        try {
            logger.info("Sending request to PSP service");
            response = restTemplate.exchange(
                    getEndpointPSP(), HttpMethod.POST, request, String.class);
            System.out.println(response.getBody());
            logger.info("Received response from PSP service");
        } catch (RestClientException | URISyntaxException e) {
            logger.error("RestTemplate error");
            e.printStackTrace();
        }

        return response.getBody();
    }

    private URI getEndpointPSP() throws URISyntaxException {
        return new URI(configuration.url() + EndpointConfig.PSP_SERVICE_BASE_URL + "/api/transactions/bank");
    }

    public String finishPayment(PCCResponseDTO dto) throws Exception {

        Payment payment = paymentService.findById(dto.getPaymentId());

        if(payment == null) {
            logger.error("Finish payment - Nonexistent payment");
            throw new Exception("Nonexistent payment");
        }

        Transaction transaction = payment.getTransaction();

        if(transaction == null) {
            logger.error("Finish payment - Nonexistent transaction");
            throw new Exception("Nonexistent transaction");
        }

        TransactionDTO responseDTO = new TransactionDTO();
        responseDTO.setPaymentId(dto.getPaymentId());
        responseDTO.setMerchantOrderId(dto.getMerchantOrderId());

        responseDTO.setAcquirerOrderId(dto.getAcquirerOrderId());
        responseDTO.setAcquirerTimestamp(dto.getAcquirerTimestamp());
        responseDTO.setIssuerOrderId(dto.getIssuerOrderId());
        responseDTO.setIssuerTimestamp(dto.getIssuerTimestamp());
        responseDTO.setStatus(dto.getStatus().toString());

        // zahtev se salje na PSP
        HttpEntity<TransactionDTO> request = new HttpEntity<>(responseDTO);
        ResponseEntity<String> response = null;

        try {
            logger.info("Finish payment - Sending request to PSP service");
            response = restTemplate.exchange(
                    "https://localhost:8085/api/transactions/bank", HttpMethod.POST, request, String.class);
            System.out.println(response.getBody());
            logger.info("Finish payment - Received response from PSP service");
        } catch (RestClientException e) {
            logger.error("Finish payment - RestTemplate error");
            e.printStackTrace();
        }

        return response.getBody();
    }
}
