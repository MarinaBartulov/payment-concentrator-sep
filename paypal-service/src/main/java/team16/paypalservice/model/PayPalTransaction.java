package team16.paypalservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.paypalservice.dto.OrderDTO;
import team16.paypalservice.enums.PayPalTransactionStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PayPalTransaction {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double price;

    @Column
    private String currency;

    @ManyToOne
    private Client client;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime executedAt;

    @Column
    @Enumerated(EnumType.STRING)
    private PayPalTransactionStatus status;

    @Column
    private String paymentId;

    //@Column
    //private Long orderId;

    /*@Column(name = "successUrl")
    private String successUrl;

    @Column(name = "errorUrl")
    private String errorUrl;

    @Column(name = "failedUrl")
    private String failedUrl;*/

    public PayPalTransaction(OrderDTO order, Client client)
    {
        this.client = client;
        this.createdAt = LocalDateTime.now();
        this.status = PayPalTransactionStatus.INITIATED;
        this.price = order.getPrice();
        this.currency = order.getCurrency();
    }

}
