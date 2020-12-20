package team16.bankpaymentservice.service;

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

import java.time.YearMonth;

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

    private ValidationService validationService;

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

        if(paymentService.findById(paymentId) == null) {
            throw new Exception("Nonexistent payment.");
        }

        Payment payment = paymentService.findById(paymentId);
        Transaction transaction = payment.getTransaction();
        Merchant merchant = transaction.getMerchant();
        Card merchantCard = cardRepository.findByCardHolderId(merchant.getId());

        OnlyAcquirerTransactionResponseDTO responseDTO = new OnlyAcquirerTransactionResponseDTO();

        // u zavisnosti od problema baciti odgovarajuci exception i odraditi odgovarajuce redirection
        try {
            validateClientInput(dto);
        } catch (InvalidDataException ide) {
            responseDTO.setRedirectionURL(merchant.getMerchantErrorUrl());
            responseDTO.setResponseMessage(ide.getMessage());
            responseDTO.setTransactionStatus(transaction.getStatus().toString());
            return responseDTO;
        } catch(InappropriateBankException ibe) {
            responseDTO.setRedirectionURL("Ide na PCC.");
            responseDTO.setTransactionStatus(transaction.getStatus().toString());
            responseDTO.setResponseMessage(ibe.getMessage());
            return responseDTO;
        } catch (Exception e) {
            responseDTO.setRedirectionURL(merchant.getMerchantErrorUrl());
            responseDTO.setTransactionStatus(transaction.getStatus().toString());
            responseDTO.setResponseMessage(e.getMessage());
            return responseDTO;
        }

        // vezati transakciju sa klijentom
        Card clientCard = cardRepository.findByPan(dto.getPan());
        Client client = (Client) clientCard.getCardHolder();
        transaction.setClient(client);

        // provera sredstava sa racuna klijenta
        try {
            checkClientFunds(clientCard, transaction);
        } catch (LackingFundsException lfe) {
            responseDTO.setRedirectionURL(merchant.getMerchantFailedUrl());
            responseDTO.setTransactionStatus(transaction.getStatus().toString());
            responseDTO.setResponseMessage(lfe.getMessage());
            return responseDTO;
        }

        // update sredstava sa kartica sa proverom sredstava klijenta
        clientCard.setAvailableFunds(clientCard.getAvailableFunds() - transaction.getAmount());
        merchantCard.setAvailableFunds(merchantCard.getAvailableFunds() + transaction.getAmount());
        transaction.setStatus(TransactionStatus.COMPLETED);

        update(clientCard);
        update(merchantCard);
        transactionService.update(transaction);

        responseDTO.setRedirectionURL(merchant.getMerchantSuccessUrl());
        responseDTO.setTransactionStatus(transaction.getStatus().toString());
        responseDTO.setResponseMessage("Transaction completed successfully.");
        return responseDTO;
    }

    private void checkClientFunds(Card clientCard, Transaction transaction) throws LackingFundsException {
        if(clientCard.getAvailableFunds() < transaction.getAmount()) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionService.update(transaction);
            throw new LackingFundsException("Lacking funds.");
        } else {
            transaction.setStatus(TransactionStatus.CREATED);
            transactionService.update(transaction);
        }
    }

    private void validateClientInput(ClientAuthDTO dto) throws Exception {
        if(!validationService.validateString(dto.getPan()) ||
            !validationService.validateString(dto.getSecurityNumber()) ||
            !validationService.validateString(dto.getCardHolderName()) ||
            !validationService.validateString(dto.getCardHolderName())) {
            throw new InvalidDataException("Invalid client information.");
        }
        Bank bank = bankService.findById(1L);
        String bankCode = bank.getCode();
        if(!dto.getPan().substring(0, 3).equals(bankCode)) {
            throw new InappropriateBankException("Client doesn't have an account in this bank.");
        }
        if(cardRepository.findByPan(dto.getPan()) == null) {
            throw new InvalidDataException("Invalid client information.");
        }
        Card card = cardRepository.findByPan(dto.getPan());
        if(!card.getSecurityCode().equals(dto.getSecurityNumber())) {
            throw new InvalidDataException("Invalid client information.");
        }
        CardOwner cardHolder = card.getCardHolder();
        if(!cardHolder.getName().equals(dto.getCardHolderName())) {
            throw new InvalidDataException("Invalid client information.");
        }
        try {
            YearMonth expDate = validationService.convertToYearMonth(dto.getExpirationDate());
            if(!expDate.equals(card.getExpirationDate())) {
                throw new InvalidDataException("Invalid client information.");
            }
            if(expDate.isBefore(YearMonth.now())) {
                throw new InvalidDataException("Card expired.");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
