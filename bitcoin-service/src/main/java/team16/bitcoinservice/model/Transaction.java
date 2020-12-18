package team16.bitcoinservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private Long orderId;
    private Date createdAt;
    private String priceCurrency;
    private Double priceAmount;
    private String receiveCurrency;
    private String receiveAmount;
    private Long paymentId;

    @ManyToOne
    private Merchant merchant;

    public Transaction(Long orderId, String priceCurrency, Double priceAmount, Merchant merchant) {
        this.orderId = orderId;
        this.priceCurrency = priceCurrency;
        this.priceAmount = priceAmount;
        this.merchant = merchant;
        this.createdAt = new Date();
        this.status = TransactionStatus.NEW;
    }
}
