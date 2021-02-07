package team16.bankpaymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.bankpaymentservice.converter.SensitiveDataConverter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@DiscriminatorValue("Merchant")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Merchant extends CardOwner {

    @Convert(converter = SensitiveDataConverter.class)
    @Column(unique = true) //, length = 30; nullable = false, zbog klijenta
    private String merchantId;

    @Column(unique = true) // nullable = false,  zbog klijenta
    private String merchantEmail;

    @Convert(converter = SensitiveDataConverter.class)
    //@Column(nullable = false) zbog klijenta
    //@Size(min = 10, max = 100)
    private String password;

}
