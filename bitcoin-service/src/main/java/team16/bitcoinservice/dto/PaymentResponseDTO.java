package team16.bitcoinservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDTO {

    private long id;
    private String status;
    private String price_currency;
    private String price_amount;
    private String receive_currency;
    private String receive_amount;
    private Date created_at;
    private String order_id;
    private String payment_url;
    private String token;


}
