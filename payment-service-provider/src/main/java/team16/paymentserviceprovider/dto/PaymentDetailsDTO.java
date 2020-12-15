package team16.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentDetailsDTO {

    private Long id;
    private String merchantId;
    private double amount;
    // mozda bi ipak trebalo i password da saljem sa LU
}
