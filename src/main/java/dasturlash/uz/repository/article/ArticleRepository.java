package dasturlash.uz.repository.article;

import dasturlash.uz.dto.article.ArticleFullInfo;
import dasturlash.uz.dto.article.ArticleShortInfo;
import dasturlash.uz.entity.article.ArticleEntity;
import dasturlash.uz.enums.ArticleStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
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

    @Query("from ArticleEntity where id = ?1 and visible = true and status = 'PUBLISHED'")
    Optional<ArticleEntity> getArticleById(String id);

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

    @Query(value = "select a.id as id\n" +
            "     , a.description as description\n" +
            "     , a.image_id as imageId\n" +
            "     , at.path as imageUrl" +
            "     , published_time as publishedDate\n" +
            "     , title as title\n" +
            "from article as a\n" +
            "         inner join article_section as ac on a.id = ac.article_id\n" +
            " inner join attach as at on at.id = a.image_id " +
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
    Page<ArticleShortInfo> getLastNArticleBySectionId(Integer sectionId, Pageable pageable);

    @Query(value = """
            select a.id                                             as id,
                   a.title                                          as title,
                   a.description                                    as description,
                   a.content                                        as content,
                   a.shared_count                                   as sharedCount,
                   a.region_id                                      as regionId,
                   r.name_uz                                        as regionName,
                   string_agg(distinct c.id::text, ',')             as categoryListId,
                   string_agg(distinct case ?2
                                           when 'EN' then c.name_en
                                           when 'RU' then c.name_ru
                                           else c.name_uz
                       end, ',')                                    as categoryListName,
                   string_agg(distinct s.id::text, ',')             as sectionListId,
                   string_agg(distinct case ?2
                                           when 'EN' then s.name_en
                                           when 'RU' then s.name_ru
                                           else s.name_uz end, ',') as sectionListName,
                   a.published_time                                 as publishedDate,
                   a.view_count                                     as viewCount
            from article as a
                     inner join public.article_category as ac on ac.article_id = a.id
                     inner join public.article_section as a_s on a_s.article_id = a.id
                     inner join category as c on c.id = ac.category_id
                     inner join section as s on s.id = a_s.section_id
                     inner join attach as at on at.id = a.image_id
                     inner join region as r on r.id = a.region_id
            where a.id = ?1
              and a.visible = true
              and status = 'PUBLISHED'
            group by a.id, r.name_uz"""
    , nativeQuery = true)
    Optional<ArticleFullInfo> getArticleByIdAndLang(String articleId, String lang);

    @Query(value = """
            select a.id as id 
                             , a.description as description
                             , a.image_id as imageId
                             , at.path as imageUrl
                             , published_time as publishedDate
                             , title as title
                        from article as a
                         inner join attach as at on at.id = a.image_id 
                        where a.id not in (?1)  
                        and visible = true 
                        and status = 'PUBLISHED' 
                        order by a.created_time desc
                        limit 12""",
            countQuery = """
                                select count(*) 
                                            from article as a
                                                     inner join attach as at on at.id = a.image_id
                                            where a.id not in (?1)  
                                            and visible = true 
                                            and status = 'PUBLISHED' 
                                            order by a.created_time desc
                    """, nativeQuery = true)
    Page<ArticleShortInfo> getLast12PublishedArticleExceptGivenIds(List<Integer> ids, Pageable pageable);

    @Query(value = """
            select a.id           as id
                 , a.description  as description
                 , a.image_id     as imageId
                 , at.path        as imageUrl
                 , published_time as publishedDate
                 , title          as title
            from article as a
                     inner join article_category as c on c.article_id = a.id
                     inner join attach as at on at.id = a.image_id
            where c.category_id = (?1)
              and visible = true
              and status = 'PUBLISHED'
            order by a.created_time desc""",
            countQuery = """
                    select count(*) 
                    from article as a
                        inner join article_category as c on c.article_id = a.id
                        inner join attach as at on at.id = a.image_id
                    where c.category_id = (?1)
                      and visible = true
                      and status = 'PUBLISHED'
                    order by a.created_time desc""", nativeQuery = true)
    Page<ArticleShortInfo> getLastNArticleByCategoryId(Integer categoryId, Pageable pageable);

    @Query(value = """
            select a.id                                as id,
                   a.title                             as title,
                   a.description                       as description,
                   a.content                           as content,
                   a.shared_count                      as sharedCount,
                   a.region_id                         as regionId,
                   r.name_uz                           as regionName,
                   string_agg(distinct c.id::text, ',')      as categoryListId,
                   string_agg(distinct c.name_uz, ',') as categoryListName,
                   string_agg(distinct s.id::text, ',')      as sectionListId,
                   string_agg(distinct s.name_uz, ',') as sectionListName,
                   a.published_time                    as publishedDate,
                   a.view_count                        as viewCount
            from article as a
                     inner join public.article_category as ac on ac.article_id = a.id
                     inner join public.article_section as a_s on a_s.article_id = a.id
                     inner join category as c on c.id = ac.category_id
                     inner join section as s on s.id = a_s.section_id
                     inner join attach as at on at.id = a.image_id
                     inner join region as r on r.id = a.region_id
            where a.region_id = ?1
              and a.visible = true
              and status = 'PUBLISHED'
            group by a.id, r.name_uz""",
            countQuery = """
            select count(*)
            from article as a
                     inner join public.article_category as ac on ac.article_id = a.id
                     inner join public.article_section as a_s on a_s.article_id = a.id
                     inner join category as c on c.id = ac.category_id
                     inner join section as s on s.id = a_s.section_id
                     inner join attach as at on at.id = a.image_id
                     inner join region as r on r.id = a.region_id
            where a.region_id = ?1
              and a.visible = true
              and status = 'PUBLISHED'
            group by a.id, r.name_uz""",
            nativeQuery = true)
    Page<ArticleFullInfo> getLastNArticleByRegionId(Integer regionId, Pageable pageable);
//
//    @Query("from ArticleEntity where title ilike ?1 or description ilike ?1")
//    Page<ArticleEntity> getLastNArticleByTagName(String tagName, PageRequest pageRequest);

    @Query(value = """
            select a.id           as id
                 , a.description  as description
                 , a.image_id     as imageId
                 , at.path        as imageUrl
                 , published_time as publishedDate
                 , title          as title
            from article as a
                     inner join attach as at on at.id = a.image_id
                     inner join public.article_section as a_s on a_s.article_id = a.id
            where a_s.section_id = ?2
              and a.id <> ?1
              and visible = true
              and status = 'PUBLISHED'
            order by a.created_time desc
            limit 4
            """, nativeQuery = true)
    List<ArticleShortInfo> getLast4ArticleBySectionIdExceptOneArticleId(String articleId, Integer sectionId);

    @Query(value = """
            select a.id           as id
                 , a.description  as description
                 , a.image_id     as imageId
                 , at.path        as imageUrl
                 , published_time as publishedDate
                 , title          as title
            from article as a
                     inner join attach as at on at.id = a.image_id
                     inner join public.article_section as a_s on a_s.article_id = a.id
            where a.id <> ?1
              and visible = true
              and status = 'PUBLISHED'
            order by a.view_count desc
            limit 4
            """, nativeQuery = true)
    List<ArticleShortInfo> getLast4MostViewedArticleExceptOneArticleId(String articleId);

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

    // do ArticleShortInfo and FullInfo -> interface

    @Query(" select a.id as id, a.title as title, a.description as description, " +
            " a.imageId as imageId, a.publishedDate as publishedDate, a.moderatorId as moderatorId, " +
            " p.name as moderName, p.surname as moderSurname " +
            " from  ArticleEntity a " +
            " inner join a.moderator as p " +
            " inner join a.articleCategory" +
            " where a.visible = true and a.status = 'PUBLISHED' " +
            " order by a.createdDate desc  limit ?1")
    List<ArticleShortInfo> getLastNArticle(int limit);

}
