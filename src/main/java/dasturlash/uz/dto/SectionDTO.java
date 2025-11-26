package dasturlash.uz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SectionDTO {
    private Integer id;

    @Positive(message = "Order must be positive")
    @NotNull(message = "Order required")
    private Integer orderNumber;

    @NotBlank(message = "Name Uz required") private String nameUz;

    @NotBlank(message = "Name Ru required") private String nameRu;

    @NotBlank(message = "Name En required") private String nameEn;

    @NotBlank(message = "Key required") private String key;

    private String name;

    private LocalDateTime createdDate;
    private Integer imageId;
}
