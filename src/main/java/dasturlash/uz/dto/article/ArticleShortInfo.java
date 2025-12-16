package dasturlash.uz.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.dto.KeyValueDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleShortInfo {
    private String title;

    private String description;

    private String imageId;

    private LocalDateTime publishedDate;

    private List<KeyValueDTO> categoryList;
}
