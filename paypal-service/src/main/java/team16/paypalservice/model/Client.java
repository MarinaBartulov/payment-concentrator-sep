package team16.paypalservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team16.paypalservice.converter.SensitiveDataConverter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    @Convert(converter = SensitiveDataConverter.class)
    private String clientId;

    @Column
    @Convert(converter = SensitiveDataConverter.class)
    private String clientSecret;
}
