package team16.bitcoinservice.model;

public enum TransactionStatus {
    NEW, //Newly created invoice. The shopper has not yet selected payment currency
    PENDING, //Shopper selected payment currency. Awaiting payment
    CONFIRMING, //Shopper transferred the payment for the invoice. Awaiting blockchain network confirmation
    PAID, //Payment is confirmed by the network, and has been credited to the merchant. Purchased goods/services can be safely delivered to the shopper
    INVALID, //Payment rejected by the network or did not confirm within 7 days
    EXPIRED, //Shopper did not pay within the required time (default: 20 minutes) and the invoice expired
    CANCELED, //Shopper canceled the invoice
    REFUNDED; //Payment was refunded to the shopper

}
