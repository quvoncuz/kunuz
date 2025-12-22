package dasturlash.uz.repository.article;

import dasturlash.uz.dto.article.ArticleLikeResponseDTO;
import dasturlash.uz.entity.article.ArticleLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLikeEntity, String> {
    Optional<ArticleLikeEntity> findArticleLikeEntityByProfile_IdAndArticleId(Integer profileId, String articleId);


    @Query("""
            select '',
                   a.articleId,
                   count(case when a.emotion = 'LIKE' then 1 end),
                   count(case when a.emotion = 'DISLIKE' then 1 end ),
                   ''
            from ArticleLikeEntity as a
            where a.articleId = ?1
            group by a.articleId
            """)
    ArticleLikeResponseDTO findAllDetailByArticleId(String articleId);



    void deleteByArticleIdAndProfileId(String articleId, Integer profileId);
}
