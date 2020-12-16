package team16.paypalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDTO {

    @NotNull
    @Email
    private String clientEmail;

   // @NotNull
   // private Long OrderId;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    private String currency;

}
