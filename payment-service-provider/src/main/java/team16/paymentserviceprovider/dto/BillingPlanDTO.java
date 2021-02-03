package team16.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.paymentserviceprovider.model.BillingPlan;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingPlanDTO {

    private Long id;

    private Double price;

    private String type;

    private String frequency;

    private Integer cyclesNumber;

    public BillingPlanDTO(BillingPlan billingPlan){
        this.id = billingPlan.getId();
        this.price = billingPlan.getPrice();
        this.type = billingPlan.getType().toString();
        this.frequency = billingPlan.getFrequency().toString();
        this.cyclesNumber = billingPlan.getCyclesNumber();
    }

}
