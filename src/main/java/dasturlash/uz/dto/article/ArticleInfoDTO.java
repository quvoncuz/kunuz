package dasturlash.uz.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.dto.KeyValueDTO;
import dasturlash.uz.enums.ArticleStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleInfoDTO {
    private String title;

    private String description;

    private String content;

    private String imageId;

    private Integer regionId;

    private Integer moderatorId;

    private Integer readTime;

    private List<KeyValueDTO> categoryList;

    private List<KeyValueDTO> sectionList;
}
