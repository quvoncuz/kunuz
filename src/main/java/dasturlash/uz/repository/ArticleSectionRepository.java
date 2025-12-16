package dasturlash.uz.repository;

import dasturlash.uz.dto.KeyValueDTO;
import dasturlash.uz.entity.ArticleCategoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleSectionRepository extends JpaRepository<ArticleCategoryEntity, String> {

    @Modifying
    @Transactional
    @Query("insert into ArticleSectionEntity(articleId, sectionId) values (?1, ?2)")
    void insertSection(String id, Integer section);

    @Modifying
    @Transactional
    int deleteAllByArticleId(String articleId);

    @Query("""
    select new dasturlash.uz.dto.KeyValueDTO(
        ac.sectionId,
        case :lang
            when 'EN' then s.nameEn
            when 'RU' then s.nameRu
            else s.nameUz
        end
    )
    from ArticleSectionEntity as ac
    inner join ac.section as s
    where ac.articleId = :articleId
""")
    List<KeyValueDTO> getAllSectionByArticleId(
            @Param("articleId") String articleId,
            @Param("lang") String lang
    );

}
