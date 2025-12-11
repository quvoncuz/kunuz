package dasturlash.uz.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class JwtDTO {
    private String username;
    private List<String> role;

    public JwtDTO(String username, List<String> role) {
        this.username = username;
        this.role = role;
    }
}
