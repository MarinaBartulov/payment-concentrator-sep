package team16.bankpaymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.bankpaymentservice.enums.TransactionStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @ManyToOne
    private Merchant merchant;

    @ManyToOne
    private Client client;

    private double amount;

    @Column
    private Long merchantOrderId;

    @Column
    private LocalDateTime merchantTimestamp;

    @Column
    private Long acquirerOrderId;

    @Column
    private LocalDateTime acquirerTimestamp;

    @Column
    private Long issuerOrderId;

    @Column
    private LocalDateTime issuerTimestamp;

}
