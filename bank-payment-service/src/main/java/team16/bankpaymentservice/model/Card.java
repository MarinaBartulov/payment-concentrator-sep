package team16.bankpaymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    private String PAN;  // broj racuna u Srbiji ima 16 brojeva

    @Column(nullable = false, length = 3)
    private String securityCode;  // Card Verification Value

    @Column(nullable = false)
    private YearMonth expirationDate;  // Datum isticanja kartice - posle kojeg ona nije validna u formi YYMM

    @Column(nullable = false)
    private double availableFunds;

    private double reservedFunds;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "card_holder_id", referencedColumnName = "id")
    private CardOwner cardHolder;

}
