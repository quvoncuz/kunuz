package dasturlash.uz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RegionDTO {
    private Integer id;

    @Positive(message = "Order number required")
    private Integer orderNumber;
    @NotEmpty(message = "nameUz number required")
    private String nameUz;
    @NotEmpty(message = "nameRu number required")
    private String nameRu;
    @NotBlank(message = "nameEn number required")
    private String nameEn;
    @NotEmpty(message = "RegionKey number required")
    private String regionKey;



    private LocalDateTime createdDate;
    private String name;
}
