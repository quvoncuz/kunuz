package dasturlash.uz.dto;

import dasturlash.uz.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProfileDTO {

    private Integer id;

    @NotBlank(message = "Name required")
    private String name;

    @NotBlank(message = "Surname required")
    private String surname;

    @NotBlank(message = "Username required")
    private String username;

    @NotBlank(message = "Photo required")
    private Integer photoId;

    @NotBlank(message = "Password required")
    private String password;

    private List<Role> roles = List.of(Role.ROLE_USER);

    private LocalDateTime createdDate;
}
