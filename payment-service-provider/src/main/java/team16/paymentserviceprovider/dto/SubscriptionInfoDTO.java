package team16.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.paymentserviceprovider.enums.SubscriptionFrequency;
import team16.paymentserviceprovider.enums.SubscriptionType;
import team16.paymentserviceprovider.model.BillingPlan;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.Subscription;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionInfoDTO {

    @NotNull
    private Long subscriptionId;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    private String currency;

    @NotNull
    @Email
    private String email;

    @NotNull
    private SubscriptionFrequency frequency;

    @NotNull
    private SubscriptionType type;

    @NotNull
    @Positive
    private Integer cyclesNumber;

    public SubscriptionInfoDTO(Subscription subscription, Merchant merchant, BillingPlan  billingPlan)
    {
        this.subscriptionId = subscription.getId();
        this.price = billingPlan.getPrice();
        this.currency = subscription.getCurrency();
        this.email = merchant.getEmail();
        this.cyclesNumber = billingPlan.getCyclesNumber();
        this.type = billingPlan.getType();
        this.frequency = billingPlan.getFrequency();
    }
}
