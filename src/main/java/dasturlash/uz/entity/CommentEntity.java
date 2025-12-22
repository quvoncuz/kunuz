package dasturlash.uz.entity;

import dasturlash.uz.entity.article.ArticleEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String content;

    @Column(name = "article_id")
    private String articleId;
    @ManyToOne
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private ArticleEntity article;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "reply_id")
    private String replyId;
    @ManyToOne
    @JoinColumn(name = "reply_id", insertable = false, updatable = false)
    private CommentEntity reply;

    @Column
    private Boolean visible = true;

    @Column(name = "created_time")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updated_time")
    private LocalDateTime updatedDate;
}
