package team16.paypalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import team16.paypalservice.enums.SubscriptionFrequency;
import team16.paypalservice.enums.SubscriptionType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {

    @NotNull
    @Email
    private String email;

    @NotNull
    private Long subscriptionId;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    private String currency;

    @NotNull
    private SubscriptionFrequency frequency;

    @NotNull
    private SubscriptionType type;

    @NotNull
    @Positive
    private Integer cyclesNumber;

}
