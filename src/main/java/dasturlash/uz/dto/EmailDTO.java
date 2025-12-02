package dasturlash.uz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailDTO {

    private Integer id;
    private String email;
    private String code;
    private String body;

    public EmailDTO(String email, String code, String body) {
        this.email = email;
        this.code = code;
        this.body = body;
    }
}
