package team16.bankpaymentservice.exceptions;

public class LackingFundsException extends Exception {

    public LackingFundsException(String message) {
        super(message);
    }
}
