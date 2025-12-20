package dasturlash.uz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class JwtDTO {
    private Integer id;
    private String username;
    private List<String> role;
}
