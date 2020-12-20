package team16.bankpaymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("Client")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Client extends CardOwner {

    private String lastName;

    private String email;
}
