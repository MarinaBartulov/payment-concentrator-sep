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
    private Long payment_id; // pazi, ovo se kreira kod banke prodavca

    private LocalDateTime payment_timestamp;

    @Column(nullable = false)
    private Long acquirer_order_id; // ovo se kreira u banci prodavca za prodavcevu transakciju

    private LocalDateTime acquirer_timestamp;

    @Column(nullable = false)
    private Long issuer_order_id; // ovo se kreira u banci prodavca za prodavcevu transakciju

    private LocalDateTime issuer_timestamp;

    private BankTransactionStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    private Order order;
}
