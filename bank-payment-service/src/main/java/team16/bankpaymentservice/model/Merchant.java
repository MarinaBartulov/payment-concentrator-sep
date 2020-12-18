package team16.bankpaymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String merchant_id;

    @Column(nullable = false, unique = true)
    private String merchantEmail;

    @Column(nullable = false)
    @Size(min = 10, max = 100)
    private String password;

    @Column(name = "success_url")
    private String merchant_success_url;

    @Column(name = "failed_url")
    private String merchant_failed_url;

    @Column(name = "error_url")
    private String merchant_error_url;
}
