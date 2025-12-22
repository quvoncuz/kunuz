package dasturlash.uz.entity.article;

import dasturlash.uz.entity.SectionEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "article_section")
public class ArticleSectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "section_id")
    private Integer sectionId;

    @Column(name = "article_id")
    private String articleId;

    @ManyToOne
    @JoinColumn(name = "section_id", insertable = false, updatable = false)
    private SectionEntity section;

    @ManyToOne
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private ArticleEntity article;
}
