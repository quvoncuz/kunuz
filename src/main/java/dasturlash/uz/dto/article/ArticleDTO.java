package dasturlash.uz.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.dto.ImageDTO;
import dasturlash.uz.dto.KeyValueDTO;
import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.dto.RegionDTO;
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
    private ImageDTO image;
    private RegionDTO region;
    private ProfileDTO moderator;
    private Integer readTime;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
    private Long viewCount;
    private Long likeCount;

    private List<KeyValueDTO> categoryList;
    private List<KeyValueDTO> sectionList;
}
