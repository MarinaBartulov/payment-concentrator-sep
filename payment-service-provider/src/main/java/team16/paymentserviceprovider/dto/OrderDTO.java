package team16.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDTO {

    private Long orderId;
    private String merchantEmail;
    private String currency;
    private double amount;
    private String merchantSuccessUrl;
    private String merchantErrorUrl;
    private String merchantFailedUrl;
}
