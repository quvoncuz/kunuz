package dasturlash.uz.dto.comment;

import dasturlash.uz.enums.Like;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentLikeResponseDTO {
    private String message;
    private String commentId;
    private Long totalLikes;
    private Long totalDislikes;
    private Like userEmotion;
}
