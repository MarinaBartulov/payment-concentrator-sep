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
    private Long billingPlanId;

    @NotNull
    private String currency;

    @NotNull
    private Long merchantId;

    @NotNull
    private String merchantEmail;
}
