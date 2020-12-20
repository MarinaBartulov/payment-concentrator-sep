package team16.bitcoinservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BitcoinPaymentDTO {

    @NotNull
    private Long orderId;
    @NotNull
    @Email
    private String merchantEmail;
    @NotNull
    @Positive
    private Double amount;
    @NotNull
    private String currency;
    private String successUrl;
    private String errorUrl;
    private String failedUrl;
}
