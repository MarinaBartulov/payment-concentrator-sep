package team16.pccservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.pccservice.enums.Status;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PCCResponseDTO {

    private Long acquirerOrderId;
    private LocalDateTime acquirerTimestamp;

    private Long issuerOrderId;
    private LocalDateTime issuerTimestamp;

    private Long merchantOrderId;
    private Long paymentId;

    private Status status;
}
