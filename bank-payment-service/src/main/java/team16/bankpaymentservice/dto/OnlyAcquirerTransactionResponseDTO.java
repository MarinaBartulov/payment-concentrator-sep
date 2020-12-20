package team16.bankpaymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OnlyAcquirerTransactionResponseDTO {

    private String transactionStatus;
    private String responseMessage;
    private String redirectionURL;
}
