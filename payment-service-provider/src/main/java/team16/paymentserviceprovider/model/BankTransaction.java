package team16.paymentserviceprovider.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long paymentId; // pazi, ovo se kreira kod banke prodavca

    @Column(nullable = false)
    private Long merchantOrderId; // pazi, ovo se kreira na PSP-u inicijalno kao Order

    @Column(nullable = false)
    private Long acquirerOrderId; // ovo se kreira u banci prodavca za prodavcevu transakciju, ali samo kad nisu u istoj banci

    private LocalDateTime acquirerTimestamp;

    @Column(nullable = false)
    private Long issuerOrderId; // ovo se kreira u banci kupca za kupcevu transakciju

    private LocalDateTime issuerTimestamp;

    private BankTransactionStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    private Order order;
}
