package team16.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MerchantPCDTO {

    @NotNull
    private String merchantName;
    @NotNull
    @Email
    private String merchantEmail;
    @NotNull
    private String activationUrl;
    @NotNull
    private String appId;
    @NotNull
    private String successUrl;
    @NotNull
    private String errorUrl;
    @NotNull
    private String failedUrl;
}
