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

    private String order_id;
    private Double price_amount;
    private String price_currency;
    private String receive_currency;
    private String title;
    private String description;
    private String callback_url;
    private String cancel_url;
    private String success_url;
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
