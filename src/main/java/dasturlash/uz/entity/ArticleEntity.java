package dasturlash.uz.entity;

import dasturlash.uz.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "article")
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(columnDefinition = "text")
    private String title;

    @Column
    private String description;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "shared_count")
    private Integer sharedCount = 0;

    @Column(name = "image_id")
    private String imageId;
    @ManyToOne
    @JoinColumn(name = "image_id", insertable = false, updatable = false)
    private AttachEntity attach;

    @Column(name = "region_id")
    private Integer regionId;
    @ManyToOne
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    private RegionEntity region;

    @Column(name = "moderator_id")
    private Integer moderatorId;
    @ManyToOne
    @JoinColumn(name = "moderator_id", insertable = false, updatable = false)
    private ProfileEntity moderator;

    @Column(name = "publisher_id")
    private Integer publisherId;
    @ManyToOne
    @JoinColumn(name = "publisher_id", insertable = false, updatable = false)
    private ProfileEntity publisher;

    @Column
    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    @Column(name = "read_time")
    private Integer readTime;

    @Column(name = "created_time")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "published_time")
    private LocalDateTime publishedDate;

    @Column
    private boolean visible = true;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @OneToMany(mappedBy = "article")
    private List<ArticleCategoryEntity> articleCategory;

    @OneToMany(mappedBy = "article")
    private List<ArticleSectionEntity> articleSection;

    @OneToMany(mappedBy = "article")
    private List<ArticleLikeEntity> likes;
}
