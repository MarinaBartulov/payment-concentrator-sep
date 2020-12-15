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

    private String merchant_id;
    private String merchant_password;
    private double amount;
    private Long merchant_order_id;
    private LocalDateTime merchant_timestamp;
    private String success_url;
    private String failed_url;
    private String error_url;
}
