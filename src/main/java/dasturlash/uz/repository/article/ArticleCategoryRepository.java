package dasturlash.uz.repository.article;

import dasturlash.uz.dto.KeyValueDTO;
import dasturlash.uz.entity.article.ArticleCategoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleCategoryRepository extends JpaRepository<ArticleCategoryEntity, String> {

    @Modifying
    @Transactional
    @Query("insert into ArticleCategoryEntity(articleId, categoryId) values(?1, ?2)")
    void insertCategory(String id, Integer category);

    @Modifying
    @Transactional
    @Query("delete from ArticleCategoryEntity where articleId = ?1 and categoryId = ?2")
    void deleteAllByArticleIdAndCategoryId(String articleId, Integer categoryId);

    @Modifying
    @Transactional
    @Query("delete from ArticleCategoryEntity where articleId = ?1")
    int deleteAllByArticleId(String id);

    @Query("""
    select new dasturlash.uz.dto.KeyValueDTO(
        ac.categoryId,
        case
            when ?2 = 'EN' then c.nameEn
            when ?2 = 'RU' then c.nameRu
            else c.nameUz
        end
    )
    from ArticleCategoryEntity ac
        join ac.category c
    where ac.article.id = ?1
""")
    List<KeyValueDTO> getAllCategoryByArticleId(String articleId, String lang);

    @Query("select a.id from ArticleCategoryEntity as a where a.articleId = ?1")
    List<Integer> getAllCategoryIdByArticleId(String id);

    Integer id(String id);
}
