package team16.paypalservice.model;

import lombok.*;
import team16.paypalservice.dto.SubscriptionDTO;
import team16.paypalservice.enums.SubscriptionFrequency;
import team16.paypalservice.enums.SubscriptionStatus;
import team16.paypalservice.enums.SubscriptionType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PayPalSubscription {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double price;

    @Column
    private String currency;

    @Column
    private Long subscriptionId; // id na psp-u

    @ManyToOne
    private Client client;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime executedAt;

    @Column
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private SubscriptionFrequency frequency;

    @Column
    @Enumerated(EnumType.STRING)
    private SubscriptionType type;

    @Column
    private Integer cyclesNumber;

    @Column
    private String billingPlanId;

    @Column
    private String billingAgreementId;

    public PayPalSubscription(SubscriptionDTO subscriptionDTO, Client client) {
        this.client = client;
        this.createdAt = LocalDateTime.now();
        this.status = SubscriptionStatus.INITIATED;
        this.price = subscriptionDTO.getPrice();
        this.currency = subscriptionDTO.getCurrency();
        this.subscriptionId = subscriptionDTO.getSubscriptionId();
        this.frequency = subscriptionDTO.getFrequency();
        this.type = subscriptionDTO.getType();
        this.cyclesNumber = subscriptionDTO.getCyclesNumber();
    }
}
