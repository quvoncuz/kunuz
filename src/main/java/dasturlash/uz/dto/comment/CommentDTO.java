package dasturlash.uz.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.dto.article.ArticleDTO;
import dasturlash.uz.entity.ProfileEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    private String id;

    private String content;

    private ArticleDTO article;

    private ProfileDTO profile;

    private String replyId;

    private Long likeCount;

    private Long dislikeCount;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private Boolean visible;
}
