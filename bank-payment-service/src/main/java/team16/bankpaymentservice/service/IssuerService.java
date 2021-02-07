package team16.bankpaymentservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team16.bankpaymentservice.dto.PCCRequestDTO;
import team16.bankpaymentservice.dto.PCCResponseDTO;
import team16.bankpaymentservice.enums.TransactionStatus;
import team16.bankpaymentservice.exceptions.InvalidDataException;
import team16.bankpaymentservice.exceptions.LackingFundsException;
import team16.bankpaymentservice.model.Card;
import team16.bankpaymentservice.model.Client;
import team16.bankpaymentservice.model.Payment;
import team16.bankpaymentservice.model.Transaction;

import java.time.LocalDateTime;

@Service
public class IssuerService {

    private ValidationService validationService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CardService cardService;

    @Autowired
    private CardOwnerService cardOwnerService;

    @Autowired
    private IPaymentService paymentService;

    Logger logger = LoggerFactory.getLogger(IssuerService.class);

    public IssuerService() { validationService = new ValidationService(); }

    public PCCResponseDTO handlePCCPaymentRequest(PCCRequestDTO dto) {


        Transaction transaction = null;

        try {
            // pronaci Transaction sa poslatim acquirer_order_is, ako je nema - greska
            transaction = transactionService.findByAcquirerOrderId(dto.getAcquirerOrderId());
        } catch (Exception e) {
            Transaction t = new Transaction();
            t.setStatus(TransactionStatus.NONEXISTENT);
            return createResponse(transactionService.update(t));
        }

        // validirati dto u smislu postojanja svih polja
        try {
            validateDTO(dto);
            validateClientCardInfo(dto);

            Card card = cardService.findByPan(dto.getClientPan());
            Client client = cardOwnerService.findClientByCardId(card.getId());

            transaction.setClient(client);
            transaction.setIssuerOrderId(dto.getPaymentRequestId());
            transaction.setIssuerTimestamp(LocalDateTime.now());
            Transaction updated1 = transactionService.update(transaction);

            checkClientFunds(card, transaction);

            Card merchantCard = cardService.findByPan(dto.getMerchantPan());

            if(merchantCard == null) {
                // nonexistant merchant card
                return createResponse(updated1);
            }

            // update sredstava sa kartica
            card.setAvailableFunds(card.getAvailableFunds() - updated1.getAmount());
            merchantCard.setAvailableFunds(merchantCard.getAvailableFunds() + updated1.getAmount());
            updated1.setStatus(TransactionStatus.COMPLETED);

            cardService.update(card);
            cardService.update(merchantCard);

            Transaction updated2 = transactionService.update(updated1);

            return createResponse(updated2);
        } catch (Exception e) {
            logger.error(e.getMessage());
            // failed transakcija
            transaction.setStatus(TransactionStatus.FAILED);
            return createResponse(transactionService.update(transaction));
        }
    }

    public PCCResponseDTO createResponse(Transaction transaction) {
        PCCResponseDTO responseDTO = new PCCResponseDTO();
        responseDTO.setAcquirerOrderId(transaction.getAcquirerOrderId());
        responseDTO.setAcquirerTimestamp(transaction.getAcquirerTimestamp());
        responseDTO.setIssuerOrderId(transaction.getIssuerOrderId());
        responseDTO.setIssuerTimestamp(transaction.getIssuerTimestamp());
        responseDTO.setMerchantOrderId(transaction.getMerchantOrderId());
        Payment payment = paymentService.findByTransactionId(transaction.getId());
        responseDTO.setPaymentId(payment.getPaymentId());
        responseDTO.setStatus(transaction.getStatus());
        return responseDTO;
    }

    private void checkClientFunds(Card clientCard, Transaction transaction) throws LackingFundsException {
        if(clientCard.getAvailableFunds() < transaction.getAmount()) {
            logger.error("Not enough available funds");
            throw new LackingFundsException("Lacking funds.");
        } else {
            transaction.setStatus(TransactionStatus.CREATED);
        }
    }

    private void validateDTO(PCCRequestDTO dto) throws InvalidDataException {
        if(!validationService.validateString(dto.getClientPan()) ||
                !validationService.validateString(dto.getSecurityNumber()) ||
                !validationService.validateString(dto.getCardHolderName()) ||
                !validationService.validateString(dto.getExpirationDate())) {
            logger.error("HandlePCCPaymentRequestInvalid  - Empty client card information");
            throw new InvalidDataException("Empty client card information");
        }
        if(!validationService.validateString(dto.getMerchantPan())) {
            logger.error("HandlePCCPaymentRequestInvalid  - Empty merchant card information");
            throw new InvalidDataException("Empty merchant card information");
        }
        if(dto.getAcquirerOrderId() == null) {
            logger.error("HandlePCCPaymentRequestInvalid  - Empty acquirer order id");
            throw new InvalidDataException("Empty acquirer order id");
        }
        if(dto.getAcquirerTimestamp() == null) {
            logger.error("HandlePCCPaymentRequestInvalid  - Empty acquirer timestamp");
            throw new InvalidDataException("Empty acquirer timestamp");
        }
        if(dto.getPaymentRequestId() == null) {
            logger.error("HandlePCCPaymentRequestInvalid  - Empty payment request id");
            throw new InvalidDataException("Empty payment request id");
        }
        if(transactionService.findByAcquirerOrderId(dto.getAcquirerOrderId()) == null) {
            logger.error("HandlePCCPaymentRequestInvalid  - Payment request with existing acquirer order id doesn't exist");
            throw new InvalidDataException("Payment request with existing acquirer order id doesn't exist");
        }
    }

    private void validateClientCardInfo(PCCRequestDTO dto) throws Exception {
        if(cardService.findByPan(dto.getClientPan()) == null) {
            logger.error("Invalid client information - Non existent client card pan");
            throw new InvalidDataException("Non existent client card pan");
        }

        Card card = cardService.findByPan(dto.getClientPan());

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
}
