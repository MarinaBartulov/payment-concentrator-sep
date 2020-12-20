package team16.bankpaymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(unique = true, length = 30)
    private String merchantId;

    @Column(unique = true)
    private String merchantEmail;

    @Size(min = 10, max = 100)
    private String password;

    @Column(name = "success_url")
    private String merchantSuccessUrl;

    @Column(name = "failed_url")
    private String merchantFailedUrl;

    @Column(name = "error_url")
    private String merchantErrorUrl;

}
