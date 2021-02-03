package team16.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.paymentserviceprovider.model.BillingPlan;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingPlanDTO {

    private Long id;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    @Positive
    private Double discount;

    @NotNull
    private String type;

    @NotNull
    private String frequency;

    @NotNull
    @Positive
    private Integer cyclesNumber;

    public BillingPlanDTO(BillingPlan billingPlan){
        this.id = billingPlan.getId();
        this.price = billingPlan.getPrice();
        this.discount = billingPlan.getDiscount();
        this.type = billingPlan.getType().toString();
        this.frequency = billingPlan.getFrequency().toString();
        this.cyclesNumber = billingPlan.getCyclesNumber();
    }

}
