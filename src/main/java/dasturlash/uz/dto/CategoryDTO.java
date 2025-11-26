package dasturlash.uz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CategoryDTO {
    private Integer id;

    @Positive(message = "Order must be positive")
    @NotNull(message = "Order require")
    private Integer orderNumber;
    @NotBlank(message = "Name Uz require")
    private String nameUz;
    @NotBlank(message = "Name Ru require")
    private String nameRu;
    @NotBlank(message = "Name En require")
    private String nameEn;
    @NotBlank(message = "Key require")
    private String key;

    private String name;
    private LocalDateTime createdDate;
}
