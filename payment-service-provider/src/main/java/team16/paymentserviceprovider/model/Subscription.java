package team16.paymentserviceprovider.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.paymentserviceprovider.dto.SubscriptionRequestDTO;
import team16.paymentserviceprovider.enums.SubscriptionFrequency;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate expirationDate;

    @Column
    private String currency;

    @ManyToOne
    private Merchant merchant;

    @ManyToOne
    private BillingPlan billingPlan;

    public Subscription(SubscriptionRequestDTO dto, Merchant merchant, BillingPlan billingPlan)
    {
        this.currency = dto.getCurrency();
        this.merchant = merchant;
        this.billingPlan = billingPlan;
        this.expirationDate = getExpirationDate(billingPlan.getCyclesNumber(), billingPlan.getFrequency());
    }

    private LocalDate getExpirationDate(Integer cycles, SubscriptionFrequency frequency){

        LocalDate expirationDate = LocalDate.now();
        if(frequency.equals(SubscriptionFrequency.MONTH))
        {
            expirationDate = expirationDate.plusMonths(cycles);
        }
        else if (frequency.equals(SubscriptionFrequency.YEAR))
        {
            expirationDate = expirationDate.plusYears(cycles);
        }
        return expirationDate;
    }
}
