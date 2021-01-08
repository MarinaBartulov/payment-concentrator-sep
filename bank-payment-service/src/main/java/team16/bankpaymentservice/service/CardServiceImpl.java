package team16.bankpaymentservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.dto.ClientAuthDTO;
import team16.bankpaymentservice.dto.OnlyAcquirerTransactionResponseDTO;
import team16.bankpaymentservice.enums.TransactionStatus;
import team16.bankpaymentservice.exceptions.InappropriateBankException;
import team16.bankpaymentservice.exceptions.InvalidDataException;
import team16.bankpaymentservice.exceptions.LackingFundsException;
import team16.bankpaymentservice.model.*;
import team16.bankpaymentservice.repository.CardRepository;

import java.time.LocalDateTime;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private BankServiceImpl bankService;

    @Autowired
    private PaymentServiceImpl paymentService;

    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private CardOwnerServiceImpl cardOwnerService;

    @Autowired
    private OrderServiceImpl orderService;

    private ValidationService validationService;

    Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);

    public CardServiceImpl() {
        validationService = new ValidationService();
    }


    @Override
    public Card findByPan(String pan) {
        return cardRepository.findByPan(pan);
    }

    @Override
    public Card create(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Card update(Card card) {
        return cardRepository.save(card);
    }

    // validirati DTO
    // porveriti na osnovu sifre banke da li je to pan iz te banke
    // ako da
    // proveriti da li se svi podaci poklapaju sa onima u bazi
    // ako ne
    // onda ide na pcc
    public OnlyAcquirerTransactionResponseDTO handleClientAuthentication(ClientAuthDTO dto, Long paymentId) throws Exception {
        System.out.println("------------------------DTO from Bank Fron to Bank for Client authentication and Transaction-------------------------------");
        if(paymentService.findById(paymentId) == null) {
            System.out.println("Nonexistent payment");
            logger.error("Nonexistent payment");
            throw new Exception("Nonexistent payment.");
        }

        Payment payment = paymentService.findById(paymentId);
        System.out.println("Payment: ");
        System.out.println(payment.getPaymentId());
        System.out.println(payment.getPaymentUrl());
        System.out.println(payment.getTransaction().getId());

        Transaction transaction = payment.getTransaction();
        System.out.println("Transaction: ");
        System.out.println(transaction.getId());
        System.out.println(transaction.getMerchant().getMerchantId());
        System.out.println(transaction.getAmount());
        System.out.println(transaction.getMerchantOrderId());
        System.out.println(transaction.getMerchantTimestamp());
        System.out.println(transaction.getStatus());

        Merchant merchant = transaction.getMerchant();
        System.out.println("Merchant from transaction: ");
        System.out.println(merchant.getId());
        System.out.println(merchant.getMerchantId());
        System.out.println(merchant.getMerchantEmail());
        System.out.println(merchant.getPassword());

        Card merchantCard = merchant.getCard();

        OnlyAcquirerTransactionResponseDTO responseDTO = new OnlyAcquirerTransactionResponseDTO();

        // u zavisnosti od problema baciti odgovarajuci exception i odraditi odgovarajuce redirection
        try {
            validateClientInput(dto);
        } catch (InvalidDataException ide) {
            responseDTO.setRedirectionURL(merchant.getMerchantErrorUrl());
            responseDTO.setResponseMessage(ide.getMessage());
            transaction.setStatus(TransactionStatus.FAILED);
            Transaction t1 = transactionService.update(transaction);
            responseDTO.setTransactionStatus(t1.getStatus().toString());
            logger.error("Invalid client data.");
            return responseDTO;
        } catch(InappropriateBankException ibe) {
            responseDTO.setRedirectionURL("Ide na PCC.");
            responseDTO.setTransactionStatus(transaction.getStatus().toString());
            responseDTO.setResponseMessage(ibe.getMessage());
            logger.info("Redirection to PCC");

            // generise se ACQUIRER_ORDER_ID i ACQUIRER_TIMESTAMP

            Order order = new Order();
            order.setAmount(transaction.getAmount());
            order.setAcquirerTimestamp(LocalDateTime.now());
            Order newOrder = orderService.create(order);
            Long acquirerOrderId = newOrder.getAcquirerOrderId();

            // zajedno sa podacima o kartici - podaci uneti sa fronta za autentifikaciju klijenta i kartice
            
            // zahtev se salje na PCC

            return responseDTO;
        } catch (Exception e) {
            responseDTO.setRedirectionURL(merchant.getMerchantErrorUrl());
            transaction.setStatus(TransactionStatus.FAILED);
            Transaction t1 = transactionService.update(transaction);
            responseDTO.setTransactionStatus(t1.getStatus().toString());
            responseDTO.setResponseMessage(e.getMessage());
            logger.error("Error validating client input");
            return responseDTO;
        }

        // vezati transakciju sa klijentom
        Card clientCard = cardRepository.findByPan(dto.getPan());
        System.out.println("Klijentova kartica:");
        System.out.println(clientCard.getId());
        System.out.println(clientCard.getExpirationDate());
        System.out.println(clientCard.getAvailableFunds());
        System.out.println(clientCard.getPAN());

        // provera sredstava sa racuna klijenta
        try {
            checkClientFunds(clientCard, transaction);
            System.out.println("Check funds proslo");
            logger.info("Enough available funds");
        } catch (LackingFundsException lfe) {
            System.out.println("Check funds nije proslo");
            responseDTO.setRedirectionURL(merchant.getMerchantFailedUrl());
            transaction.setStatus(TransactionStatus.FAILED);
            Transaction t1 = transactionService.update(transaction);
            responseDTO.setTransactionStatus(t1.getStatus().toString());
            responseDTO.setResponseMessage(lfe.getMessage());
            logger.error("Not enough available funds");
            return responseDTO;
        }

        // update sredstava sa kartica sa proverom sredstava klijenta
        clientCard.setAvailableFunds(clientCard.getAvailableFunds() - transaction.getAmount());
        merchantCard.setAvailableFunds(merchantCard.getAvailableFunds() + transaction.getAmount());
        transaction.setStatus(TransactionStatus.COMPLETED);

        update(clientCard);
        //update(merchantCard);
        transactionService.update(transaction);
        System.out.println(" Updated Transaction: ");
        System.out.println(transaction.getId());
        System.out.println(transaction.getStatus());

        System.out.println("update client funds: ");
        System.out.println(clientCard.getId());
        System.out.println(clientCard.getAvailableFunds());

        responseDTO.setRedirectionURL(merchant.getMerchantSuccessUrl());
        responseDTO.setTransactionStatus(transaction.getStatus().toString());
        responseDTO.setResponseMessage("Transaction completed successfully.");
        logger.info("Transaction completed successfully");
        return responseDTO;
    }

    private void checkClientFunds(Card clientCard, Transaction transaction) throws LackingFundsException {
        if(clientCard.getAvailableFunds() < transaction.getAmount()) {
            System.out.println("Lacking funds.");
            logger.error("Not enough available funds");
            throw new LackingFundsException("Lacking funds.");
        } else {
            transaction.setStatus(TransactionStatus.CREATED);
            transactionService.update(transaction);
            System.out.println("Not Lacking funds.");
        }
    }

    private void validateClientInput(ClientAuthDTO dto) throws Exception {
        if(!validationService.validateString(dto.getPan()) ||
            !validationService.validateString(dto.getSecurityNumber()) ||
            !validationService.validateString(dto.getCardHolderName()) ||
            !validationService.validateString(dto.getCardHolderName())) {
            System.out.println("Invalid client information.");
            logger.error("Invalid client information");
            throw new InvalidDataException("Invalid client information.");
        }
        Bank bank = bankService.findById(1L);
        String bankCode = bank.getCode();
        System.out.println("Current bank");
        System.out.println(bankCode);
        if(!dto.getPan().substring(0, 3).equals(bankCode)) {
            System.out.println("Client doesn't have an account in this bank.");
            logger.error("Client doesn't have an account in this bank");
            throw new InappropriateBankException("Client doesn't have an account in this bank.");
        }
        if(cardRepository.findByPan(dto.getPan()) == null) {
            System.out.println("Invalid client information.");
            logger.error("Invalid client information - Non existent client account");
            throw new InvalidDataException("Invalid client information.");
        }

        Card card = cardRepository.findByPan(dto.getPan());
        System.out.println("Card:");
        System.out.println(card.getId());
        System.out.println(card.getPAN());

        if(!card.getSecurityCode().equals(dto.getSecurityNumber())) {
            System.out.println("Invalid client information. Security code doesnt match");
            logger.error("Invalid client information - security codes don't match");
            throw new InvalidDataException("Invalid client information.");
        }

        Client client = cardOwnerService.findClientByCardId(card.getId());
        if(!client.getName().equals(dto.getCardHolderName())) {
            System.out.println("Invalid client information. CHD name doesnt match");
            logger.error("Invalid client information - Card Holder Name not valid");
            throw new InvalidDataException("Invalid client information.");
        }
        if(!validationService.convertToYearMonthFormat(dto.getExpirationDate()).equals(card.getExpirationDate())) {
            System.out.println("Invalid client information. Not good year month");
            logger.error("Invalid client information - false expiration date");
            throw new InvalidDataException("Invalid client information.");
        }
    }
}
