package team16.paymentserviceprovider.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.paymentserviceprovider.enums.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long merchantOrderId; //ovo je id od ordera sa klijentske aplikacije (LA)
    @Column(nullable = false)
    private double amount;
    private String currency;
    private LocalDateTime merchantOrderTimestamp;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Merchant merchant;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PaymentMethod paymentMethod;


}
