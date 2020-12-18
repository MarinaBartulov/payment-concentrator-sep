package team16.paymentserviceprovider.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


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
    private String merchantId;

    @Column(nullable = false, unique = true)
    private String merchantEmail;

    @Column(nullable = false)
    @Size(min = 10, max = 100)
    private String password;

    @Column(name = "success_url")
    private String merchantSuccessUrl;

    @Column(name = "failed_url")
    private String merchantFailedUrl;

    @Column(name = "error_url")
    private String merchantErrorUrl;

    @OneToMany(mappedBy = "merchant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();
}
