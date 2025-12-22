package dasturlash.uz.dto.article;

import dasturlash.uz.enums.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArticleLikeDTO {
    private String articleId;
    private Like emotion;
}
