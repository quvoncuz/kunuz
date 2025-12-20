package dasturlash.uz.repository;

import dasturlash.uz.dto.filter.AdminFilterDTO;
import dasturlash.uz.dto.filter.FilterDTO;
import dasturlash.uz.dto.filter.FilterResultDTO;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.util.SpringSecurityUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ArticleCustomRepository {

    @Autowired
    private EntityManager entityManager;

    public FilterResultDTO<Object[]> filter(FilterDTO dto, int page, int size, boolean isModerator) {
        StringBuilder selectQuery = new StringBuilder(
                """
                        select a.id as id
                             , a.description as description
                             , a.imageId as imageId
                             , a.publishedDate as publishedDate
                             , a.title as title
                        from ArticleEntity as a
                           
                        """
        );
        StringBuilder countQuery = new StringBuilder("""
            select count(*) from ArticleEntity as a
            """
        );


        StringBuilder conditionQuery = new StringBuilder(
                " where a.visible = true "
        );

        Map<String, Object> map = new HashMap<>();

        if (isModerator){
            conditionQuery.append(" and a.moderatorId = :moderatorId");
            map.put("moderatorId", SpringSecurityUtil.currentProfileId());
        } else {
            conditionQuery.append(" and a.status = :status ");
            map.put("status", ArticleStatus.PUBLISHED);
        }

        if (dto.getTitle() != null) {
            conditionQuery.append(" and lower(a.title) like :title");
            map.put("title", ("%" + dto.getTitle().toLowerCase() + "%"));
        }
        if (dto.getSectionId() != null) {
            selectQuery.append(" inner join ArticleSectionEntity ase on ase.articleId = a.id  ");
            countQuery.append(" inner join ArticleSectionEntity ase on ase.articleId = a.id  ");

            conditionQuery.append(" and  ase.sectionId =:sectionId");
            map.put("sectionId", dto.getSectionId());
        }
        if (dto.getCategoryId() != null) {
            selectQuery.append(" inner join ArticleCategoryEntity ace on ace.articleId = a.id  ");
            countQuery.append(" inner join ArticleCategoryEntity ace on ace.articleId = a.id  ");

            conditionQuery.append(" and  ace.categoryId =:categoryId");
            map.put("categoryId", dto.getCategoryId());
        }
        if (dto.getRegionId() != null) {
            conditionQuery.append(" and a.regionId = :regionId");
            map.put("regionId", dto.getRegionId());
        }
        if (dto.getPublishedDateFrom() != null) {
            conditionQuery.append(" and a.publishedDate >= :publishedDateFrom");
            map.put("publishedDateFrom", LocalDateTime.of(dto.getPublishedDateFrom(), LocalTime.MIN));
        }
        if (dto.getPublishedDateTo() != null) {
            conditionQuery.append(" and a.publishedDate <= :publishedDateTo");
            map.put("publishedDateTo", LocalDateTime.of(dto.getPublishedDateTo(), LocalTime.MAX));
        }
        if (dto.getCreatedDateFrom() != null) {
            conditionQuery.append(" and a.createdDate >= :createdDateFrom");
            map.put("createdDateFrom", LocalDateTime.of(dto.getCreatedDateFrom(), LocalTime.MIN));
        }
        if (dto.getPublishedDateTo() != null) {
            conditionQuery.append(" and a.createdDateTo <= :createdDateTo");
            map.put("createdDateTo", LocalDateTime.of(dto.getCreatedDateTo(), LocalTime.MAX));
        }

        String selectQ = selectQuery.append(conditionQuery).toString();
        String countQ = countQuery.append(conditionQuery).toString();

        Query querySelect = entityManager.createQuery(selectQ);
        Query queryCount = entityManager.createQuery(countQ);

        map.forEach(querySelect::setParameter);
        map.forEach(queryCount::setParameter);

        querySelect.setFirstResult(page * size);
        querySelect.setMaxResults(size);

        List<Object[]> resultList = querySelect.getResultList();

        Long totalCount = (Long) queryCount.getSingleResult();

        return new FilterResultDTO<>(resultList, totalCount);
    }

    public FilterResultDTO<Object[]> adminFilter(AdminFilterDTO dto, int page, int size) {

        StringBuilder selectQuery = new StringBuilder(
                """
                        select a.id as id
                             , a.description as description
                             , a.imageId as imageId
                             , a.publishedDate as publishedDate
                             , a.title as title
                        from ArticleEntity as a
                           
                        """
        );
        StringBuilder countQuery = new StringBuilder("""
            select count(*) from ArticleEntity as a
            """
        );

        StringBuilder conditionQuery = new StringBuilder("""
                 where visible = true 
                """);

        Map<String, Object> map = new HashMap<>();

        if (dto.getTitle() != null) {
            conditionQuery.append(" and lower(a.title) = :title");
            map.put("title", "%"+dto.getTitle().toLowerCase()+"%");
        }
        if (dto.getCategoryId() != null) {
            conditionQuery.append(" and :categoryId in (select a_c.categoryId from ArticleCategoryEntity as a_c where a_c.articleId = a.id)");
            map.put("categoryId", dto.getCategoryId());
        }
        if (dto.getSectionId() != null) {
            conditionQuery.append(" and :sectionId in (select _as.sectionId from ArticleSectionEntity as _ac where a_c.articleId = a.id)");
            map.put("sectionId", dto.getSectionId());
        }
        if (dto.getRegionId() != null) {
            conditionQuery.append(" and a.regionId = :regionId");
            map.put("regionId", dto.getRegionId());
        }
        if (dto.getCreatedDateFrom() != null) {
            conditionQuery.append(" and a.createdDate >= :createdDateFrom");
            map.put("createdDateFrom", LocalDateTime.of(dto.getCreatedDateFrom(), LocalTime.MIN));
        }
        if (dto.getCreatedDateTo() != null) {
            conditionQuery.append(" and a.createdDate <= :createdDateTo");
            map.put("createdDateTo", LocalDateTime.of(dto.getCreatedDateTo(), LocalTime.MAX));
        }
        if (dto.getPublishedDateFrom() != null) {
            conditionQuery.append(" and a.publishedDate >= :publishedDateFrom");
            map.put("publishedDateFrom", LocalDateTime.of(dto.getPublishedDateFrom(), LocalTime.MIN));
        }
        if (dto.getPublishedDateTo() != null) {
            conditionQuery.append(" and a.publishedDate <= :publishedDateTo");
            map.put("publishedDateTo", LocalDateTime.of(dto.getPublishedDateTo(), LocalTime.MAX));
        }
        if (dto.getModeratorId() != null){
            conditionQuery.append(" and a.moderatorId = :moderatorId");
            map.put("moderatorId", dto.getModeratorId());
        }
        if (dto.getPublisherId() != null){
            conditionQuery.append(" and a.publisherId = :publisherId");
            map.put("publisherId", dto.getPublisherId());
        }
        if (dto.getStatus() != null){
            conditionQuery.append(" and a.status = :status");
            map.put("status", dto.getStatus());
        }

        String selectQ = selectQuery.append(conditionQuery).toString();
        String countQ = countQuery.append(conditionQuery).toString();

        Query querySelect = entityManager.createQuery(selectQ);
        querySelect.setFirstResult(page*size);
        querySelect.setMaxResults(size);
        map.forEach(querySelect::setParameter);

        Query queryCount = entityManager.createQuery(countQ);
        map.forEach(queryCount::setParameter);

        List<Object[]> resultList = querySelect.getResultList();
        Long totalCount = (Long) queryCount.getSingleResult();


        return new FilterResultDTO<>(resultList, totalCount);
    }
}
