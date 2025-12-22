package dasturlash.uz.entity;

import dasturlash.uz.enums.Like;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comment_like")
public class CommentLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne()
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "comment_id")
    private String commentId;
    @ManyToOne()
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    private CommentEntity comment;

    @Column(name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "emotion")
    @Enumerated(EnumType.STRING)
    private Like emotion;
}
