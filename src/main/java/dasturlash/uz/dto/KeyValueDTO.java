package dasturlash.uz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KeyValueDTO {
    private Integer id;
    private String value;

    public KeyValueDTO(Integer id, String value) {
        this.id = id;
        this.value = value;
    }
}
