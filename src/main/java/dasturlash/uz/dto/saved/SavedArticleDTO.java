package dasturlash.uz.dto.saved;

import dasturlash.uz.dto.article.ArticleDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SavedArticleDTO {
    private String id;
    private ArticleDTO article;
    private LocalDateTime savedTime;
}
