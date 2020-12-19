package team16.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderInfoDTO {

    private Long orderId;
    private String merchantEmail;
    private Double amount;
    private String currency;
    private String successUrl;
    private String errorUrl;
    private String failedUrl;
}
