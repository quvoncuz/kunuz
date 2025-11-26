package dasturlash.uz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileInfoDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String username;

    private Integer photoId;

    private List<String> roles;

}
