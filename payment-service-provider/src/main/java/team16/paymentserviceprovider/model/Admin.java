package team16.paymentserviceprovider.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Admin")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends User {

    private String adminFirstName;
    private String adminLastName;
}
