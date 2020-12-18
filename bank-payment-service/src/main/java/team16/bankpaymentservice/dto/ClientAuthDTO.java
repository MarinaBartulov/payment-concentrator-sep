package team16.bankpaymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientAuthDTO {

    private String pan;
    private String securityNumber;
    private String cardHolderName;
    private String expirationDate;

}
