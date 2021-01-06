package team16.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestDTO {

    @NotNull
    @Positive
    private Double price;

    @NotNull
    private String currency;

    @NotNull
    @Email
    private String merchantEmail;

    @NotNull
    private String merchantId;
}
