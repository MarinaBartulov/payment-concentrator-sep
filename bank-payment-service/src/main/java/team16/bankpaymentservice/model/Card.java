package team16.bankpaymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.bankpaymentservice.converter.SensitiveDataConverter;

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

    @Convert(converter = SensitiveDataConverter.class)
    @Column(nullable = false, unique = true) // , length = 16
    private String PAN;  // broj racuna u Srbiji ima 16 brojeva

    @Convert(converter = SensitiveDataConverter.class)
    @Column(nullable = false) //, length = 3
    private String securityCode;  // Card Verification Value

    @Column(nullable = false)
    private String expirationDate;  // Datum isticanja kartice - posle kojeg ona nije validna u formi YYMM

    @Column(nullable = false)
    private double availableFunds;

}
