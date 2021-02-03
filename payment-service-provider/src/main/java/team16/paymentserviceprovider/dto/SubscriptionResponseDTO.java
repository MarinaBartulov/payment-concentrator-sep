package team16.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.paymentserviceprovider.enums.SubscriptionFrequency;
import team16.paymentserviceprovider.model.BillingPlan;
import team16.paymentserviceprovider.model.Merchant;
import team16.paymentserviceprovider.model.Subscription;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDTO {

    private Long subscriptionId;

    private LocalDate expirationDate;

    private Double price;

    private Integer cycles;

    private SubscriptionFrequency frequency;

    private String currency;

    private String merchantEmail;

    private String redirectURL;

    public SubscriptionResponseDTO(Subscription subscription, BillingPlan billingPlan, Merchant merchant, String redirectURL){
        this.subscriptionId = subscription.getId();
        this.price = billingPlan.getPrice();
        this.currency = subscription.getCurrency();
        this.cycles = billingPlan.getCyclesNumber();
        this.frequency = billingPlan.getFrequency();
        this.merchantEmail = merchant.getEmail();
        this.redirectURL = redirectURL;
        this.expirationDate = subscription.getExpirationDate();
    }


}
