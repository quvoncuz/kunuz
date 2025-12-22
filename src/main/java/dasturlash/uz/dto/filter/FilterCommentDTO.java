package dasturlash.uz.dto.filter;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FilterCommentDTO {
    LocalDate createdDateFrom;
    LocalDate createdDateTo;
    Integer profileId;
    String articleId;
}
