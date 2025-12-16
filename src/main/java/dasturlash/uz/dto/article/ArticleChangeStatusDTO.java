package dasturlash.uz.dto.article;

import dasturlash.uz.enums.ArticleStatus;
import lombok.Getter;

@Getter
public class ArticleChangeStatusDTO {
    private String articleId;
    private ArticleStatus status;
}
