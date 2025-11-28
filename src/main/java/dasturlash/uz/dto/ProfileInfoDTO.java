package dasturlash.uz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileInfoDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String username;

    private Integer photoId;

    private List<Role> roles;

}
