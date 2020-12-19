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
    private String merchantEmail;
    private Double amount;
    private String currency;
    private String successUrl;
    private String errorUrl;
    private String failedUrl;
}
