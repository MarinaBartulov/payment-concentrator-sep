package team16.bitcoinservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormFieldDTO {

    private String name;
    private String label;
    private FormFieldType type;
    private boolean required;
}
