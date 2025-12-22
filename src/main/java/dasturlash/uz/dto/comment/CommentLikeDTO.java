package dasturlash.uz.dto.comment;

import dasturlash.uz.enums.Like;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentLikeDTO {
    private String commentId;
    private Like emotion;
}
