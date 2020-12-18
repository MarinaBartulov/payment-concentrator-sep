package team16.bitcoinservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BitcoinPaymentDTO {

    private Long orderId;
    private String email;
    private Double paymentAmount;
    private String paymentCurrency;
}
