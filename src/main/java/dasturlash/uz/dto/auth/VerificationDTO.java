package dasturlash.uz.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationDTO {
    @NotBlank private String username;
    @NotBlank private String code;
}
