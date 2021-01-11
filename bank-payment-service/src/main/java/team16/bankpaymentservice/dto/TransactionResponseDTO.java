package team16.bankpaymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionResponseDTO {

    private String transactionStatus;
    private String responseMessage;
    private String redirectionURL;
}
