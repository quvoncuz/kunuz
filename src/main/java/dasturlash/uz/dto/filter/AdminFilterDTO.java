package dasturlash.uz.dto.filter;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdminFilterDTO {
    private String title;
    private Integer regionId;
    private Integer categoryId;
    private Integer sectionId;
    private LocalDate publishedDateFrom;
    private LocalDate publishedDateTo;
    private LocalDate createdDateFrom;
    private LocalDate createdDateTo;
    private Integer moderatorId;
    private Integer publisherId;
    private String status;
}
