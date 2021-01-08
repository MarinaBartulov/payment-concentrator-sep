package team16.paymentserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {

    @NotNull
    private String oldPassword;
    @NotNull
    @Size(min = 10, message = "Password must be at least 10 characters long.")
    private String newPassword;
    @NotNull
    private String confirmNewPassword;
}
