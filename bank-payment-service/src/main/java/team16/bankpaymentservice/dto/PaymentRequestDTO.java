package team16.bankpaymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentRequestDTO {

    private String merchantId;
    private String merchantEmail;
    private String merchantPassword;
    private double amount;
    private Long merchantOrderId;
    private LocalDateTime merchantTimestamp;
    private String successUrl;
    private String failedUrl;
    private String errorUrl;
}
