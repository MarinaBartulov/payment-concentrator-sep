package team16.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppDTO {

    @NotNull
    private String appName;
    @NotNull
    private String webAddress;
    @NotNull
    @Email
    private String officialEmail;
    private List<String> paymentMethods;
    private String appId;

}
