package team16.bitcoinservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {

    //Merchant's custom order ID
    private String order_id;
    //The price set by the merchant
    private Double price_amount;
    //currency code which defines the currency in which you wish to price your merchandise
    //EUR, USD, CAD, BTC, ETH
    private String price_currency;
    //currency code which defines the currency in which you wish to receive your settlements
    //DO_NOT_CONVERT to keep payments received in original currency
    private String receive_currency;
    //Example: product title (Apple iPhone 6), order id (MyShop Order #12345)
    private String title;
    //More details about this order
    private String description;
    //Send an automated message to Merchant URL when order status is changed
    private String callback_url;
    //Redirect to Merchant URL when buyer cancels the order
    private String cancel_url;
    //Redirect to Merchant URL after successful payment
    private String success_url;
    //Your custom token to validate payment callback (notification)
    private String token;

    public PaymentRequestDTO(String order_id, Double price_amount, String price_currency, String receive_currency, String callback_url, String cancel_url, String success_url, String token) {
        this.order_id = order_id;
        this.price_amount = price_amount;
        this.price_currency = price_currency;
        this.receive_currency = receive_currency;
        this.callback_url = callback_url;
        this.cancel_url = cancel_url;
        this.success_url = success_url;
        this.token = token;
    }
}
