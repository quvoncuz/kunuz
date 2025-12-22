package dasturlash.uz.dto.comment;

import java.time.LocalDateTime;

public interface CommentShortInfo {
    String getId();
    LocalDateTime getCreatedDate();
    LocalDateTime getUpdatedDate();
    Integer getProfileId();
    String getProfileName();
    String getProfileSurname();
    String getProfileImageId();
    String getProfileImageUrl();
    Long getLikeCount();
    Long getDislikeCount();
}
