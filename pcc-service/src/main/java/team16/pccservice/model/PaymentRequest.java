package team16.pccservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.pccservice.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class PaymentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long acquirerOrderID; // id oredr-a kreiranog u banci prodavca - Acquirer

    private LocalDateTime acquirerTimestamp;

    private Long issuerOrderID; // id oredr-a kreirane u banci kupca - Issuer

    private LocalDateTime issuerTimestamp;

    private Long merchantOrderId;

    @Column
    private Long paymentId;

    @ManyToOne
    private Bank acquirerBank;

    @ManyToOne
    private Bank issuerBank;

    private LocalDateTime createTime;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

}
