package team16.paymentserviceprovider.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String appId; // ovo se salje sa literary association
    private String appName;
    private String webAddress;
    @Column(unique = true)
    private String officialEmail;
    @OneToMany(mappedBy="app")
    private Set<Merchant> merchants = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "apps_paymentMethods", joinColumns = @JoinColumn(name = "app_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "payment_method_id", referencedColumnName = "id"))
    private Set<PaymentMethod> paymentMethods = new HashSet<>();
}
