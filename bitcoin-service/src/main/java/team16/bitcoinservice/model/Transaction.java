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

    private Long orderId; //nas id od ordera
    private Date createdAt;
    private String priceCurrency;
    private Double priceAmount;
    private String receiveCurrency;
    private Double receiveAmount;
    private Long paymentId; //id koji dobijemo od CoinGate-a
    private String successUrl;
    private String errorUrl;
    private String failedUrl;

    @ManyToOne
    private Merchant merchant;

}
