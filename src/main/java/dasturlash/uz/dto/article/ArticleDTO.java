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
public class ArticleDTO {
    private String id;

    private String title;

    private String description;

    private String content;

    private Integer sharedCount;

    private String imageId;

    private Integer regionId;

    private Integer moderatorId;

    private Integer publisherId;

    private ArticleStatus status;

    private Integer readTime;

    private LocalDateTime createdDate;

    private LocalDateTime publishedDate;

    private Long viewCount;

    private List<KeyValueDTO> categoryList;

    private List<KeyValueDTO> sectionList;
}
