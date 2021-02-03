package team16.paymentserviceprovider.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.paymentserviceprovider.dto.BillingPlanDTO;
import team16.paymentserviceprovider.enums.SubscriptionFrequency;
import team16.paymentserviceprovider.enums.SubscriptionType;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BillingPlan {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double price;

    @Column
    private Double discount;

    @Column
    @Enumerated(EnumType.STRING)
    private SubscriptionType type;

    @Column
    @Enumerated(EnumType.STRING)
    private SubscriptionFrequency frequency;

    @Column
    private Integer cyclesNumber;

    @ManyToOne
    private Merchant merchant;

    @Column
    private Boolean isDefault;

    public BillingPlan(BillingPlanDTO billingPlanDTO, Merchant merchant){
        this.price = billingPlanDTO.getPrice();
        this.discount = billingPlanDTO.getDiscount();
        this.cyclesNumber = billingPlanDTO.getCyclesNumber();
        this.isDefault = false;
        this.merchant = merchant;
        this.frequency = SubscriptionFrequency.valueOf(billingPlanDTO.getFrequency());
        this.type = SubscriptionType.valueOf(billingPlanDTO.getType());
    }


}
