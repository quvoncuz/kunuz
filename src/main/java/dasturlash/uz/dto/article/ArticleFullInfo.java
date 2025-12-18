package dasturlash.uz.dto.article;

import java.time.LocalDateTime;

public interface ArticleFullInfo {
    String getId();
    String getTitle();
    String getDescription();
    String getContent();
    Integer getSharedCount();
    Integer getRegionId();
    String getRegionName();
    String getCategoryListId();
    String getCategoryListName();
    String getSectionListId();
    String getSectionListName();
    Integer getModeratorId();
    String getModeratorName();
    LocalDateTime getPublishedDate();
    Long getViewCount();
    Integer getReadTime();
    Long getLikeCount();
}
