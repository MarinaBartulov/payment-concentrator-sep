package team16.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.paymentserviceprovider.model.PaymentMethod;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {

    private Long id;
    private String name;

    public PaymentMethodDTO(PaymentMethod paymentMethod){
        this.id = paymentMethod.getId();
        this.name = paymentMethod.getName();
    }
}
