package dasturlash.uz.repository;

import dasturlash.uz.dto.saved.SavedArticleMapper;
import dasturlash.uz.entity.SavedArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedArticleRepository extends JpaRepository<SavedArticleEntity, String> {
    Optional<SavedArticleEntity> findByArticleId(String articleId);

    @Query("""
            select sa.id as id,
                   sa.article.id as articleId,
                   sa.article.title as articleTitle,
                   sa.article.description as articleDescription,
                   sa.article.imageId as articleImageId,
                   at.path,
                   sa.createdDate
            from SavedArticleEntity as sa
            left join AttachEntity as at on at.id = sa.article.imageId
            """)
    List<SavedArticleMapper> findSavedArticleEntitiesByProfileId(Integer profileId);
}
