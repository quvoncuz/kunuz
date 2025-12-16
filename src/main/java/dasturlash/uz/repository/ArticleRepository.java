package dasturlash.uz.repository;

import dasturlash.uz.entity.ArticleEntity;
import dasturlash.uz.enums.ArticleStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String> {

    @Modifying
    @Transactional
    @Query("update ArticleEntity set visible = false where id = ?1")
    int changeVisibleFalse(String id);

    @Modifying
    @Transactional
    @Query("update ArticleEntity set status = ?2 where id = ?1")
    int changeStatus(String id, ArticleStatus status);

    @Modifying
    @Transactional
    @Query("update ArticleEntity set publisherId = ?2 where id = ?1")
    void setPublisherId(String id, Integer publisherId);

    @Modifying
    @Transactional
    @Query("update ArticleEntity set publishedDate = ?2 where id = ?1")
    void setPublishedDate(String id, LocalDateTime publishedDate);

    @Query(value = "select a.id\n" +
            "     , content\n" +
            "     , created_time\n" +
            "     , description\n" +
            "     , image_id\n" +
            "     , moderator_id\n" +
            "     , published_time\n" +
            "     , publisher_id\n" +
            "     , read_time\n" +
            "     , region_id\n" +
            "     , shared_count\n" +
            "     , status\n" +
            "     , title\n" +
            "     , view_count\n" +
            "     , visible\n" +
            "from article as a\n" +
            "         inner join article_section as ac on a.id = ac.article_id\n" +
            "where ac.section_id = ?1 \n " +
            "and visible = true \n" +
            "and status = 'PUBLISHED' \n" +
            "order by a.created_time desc\n"
            , countQuery = "select count(*) " +
            "from article as a\n" +
            "         inner join article_section as ac on a.id = ac.article_id\n" +
            "where ac.section_id = ?1 \n " +
            "and visible = true \n" +
            "and status = 'PUBLISHED' \n" +
            "order by a.created_time desc\n",
            nativeQuery = true)
    Page<ArticleEntity> getLastNArticleBySectionId(Integer sectionId, Pageable pageable);


    @Query("""
            select a from ArticleEntity as a 
                        where a.visible = true and a.status = 'PUBLISHED' 
                                    and a.id not in (?1) order by a.createdDate desc
            """)
    Page<ArticleEntity> getLast12PublishedArticleExceptGivenIds(List<Integer> ids, Pageable pageable);

    @Query("""
            select a from ArticleEntity as a 
                        inner join ArticleCategoryEntity as ac on a.id = ac.articleId
            where ac.categoryId = ?1 and a.visible = true and a.status = 'PUBLISHED'
                        order by a.createdDate desc 
            """)
    Page<ArticleEntity> getLastNArticleByCategoryId(Integer categoryId, Pageable pageable);

    @Query("""
            select a from ArticleEntity as a 
                        where a.regionId = ?1 and 
                                    a.visible = true and 
                                    a.status = 'PUBLISHED' 
                                    order by a.createdDate desc 
            """)
    Page<ArticleEntity> getLastNArticleByRegionId(Integer regionId, Pageable pageable);

    @Query("from ArticleEntity where id = ?1 and visible = true and status = 'PUBLISHED'")
    Optional<ArticleEntity> getArticleById(String id);

    @Query("from ArticleEntity where title ilike ?1 or description ilike ?1")
    Page<ArticleEntity> getLastNArticleByTagName(String tagName, PageRequest pageRequest);

    @Query(value = """
            select a from article as a\s
                    inner join article_section as ac on a.id = ac.article_id 
            where ac.sectionId = ?2 and a.id <> ?1
            order by a.created_time desc
            limit 4
            """, nativeQuery = true)
    List<ArticleEntity> getLast4ArticleBySectionIdExceptOneArticleId(String articleId, Integer sectionId);

    @Query(value = """
            select *
            from article
            order by view_count desc
            limit 4;
            """, nativeQuery = true)
    List<ArticleEntity> getLast4MostViewedArticleExceptOneArticleId(String articleId);

    @Modifying
    @Transactional
    @Query("""
            update ArticleEntity as a set a.viewCount = a.viewCount+1 where a.id = ?1
            """)
    void increaseViewCountByArticleId(String articleId);

    @Query("select a.viewCount from ArticleEntity  as a where a.id = ?1")
    Long getArticleViewCountByArticleId(String articleId);

    @Modifying
    @Transactional
    @Query("""
            update ArticleEntity as a set a.sharedCount = a.sharedCount+1 where a.id = ?1
            """)
    void increaseShareCountByArticleId(String articleId);

    @Query("select a.sharedCount from ArticleEntity  as a where a.id = ?1")
    Long getArticleShareCountByArticleId(String articleId);
}
