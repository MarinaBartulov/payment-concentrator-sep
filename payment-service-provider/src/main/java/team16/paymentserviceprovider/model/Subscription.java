package team16.paymentserviceprovider.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.paymentserviceprovider.dto.SubscriptionRequestDTO;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    /*@Column
    private Long subscriptionId;

    @Column
    private Date expirationDate;

    @Column
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;*/

    @Column
    private Double price;

    @Column
    private String currency;

    @ManyToOne
    private Merchant merchant;

    public Subscription(SubscriptionRequestDTO dto, Merchant merchant)
    {
        this.price = dto.getPrice();
        this.currency = dto.getCurrency();
        this.merchant = merchant;
    }
}
