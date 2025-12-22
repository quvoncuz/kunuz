package dasturlash.uz.dto.article;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.enums.Like;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleLikeResponseDTO {
    private String message;
    private String articleId;
    private Long totalLikes;
    private Long totalDislikes;
    private Like userEmotion;
}
