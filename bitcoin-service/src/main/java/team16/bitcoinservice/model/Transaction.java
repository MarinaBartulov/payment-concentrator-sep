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
    private String priceAmount;
    private String receiveCurrency;
    private String receiveAmount;

    @ManyToOne
    private Merchant merchant;

}
