package team16.bankpaymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionDTO {

    private Long paymentId;
    private Long merchantOrderId;
    private Long acquirerOrderId;
    private LocalDateTime acquirerTimestamp;
    private Long issuerOrderId;
    private LocalDateTime issuerTimestamp;
    private String status;
}
