package dasturlash.uz.dto.saved;

import java.time.LocalDateTime;

public interface SavedArticleMapper {
    String getId();
    String getArticleId();
    String getArticleTitle();
    String getArticleDescription();
    String getArticleImageId();
    String getArticleImageUrl();
    LocalDateTime getSavedDate();
}
