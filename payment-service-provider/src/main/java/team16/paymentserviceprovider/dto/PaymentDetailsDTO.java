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

    // mozda mi ne treba id
    private Long id;
    private String merchantId;
    private String merchantPassword;
    private double amount;
    // mozda bi ipak trebalo i password da saljem sa LU
}
