package dasturlash.uz.repository.comment;

import dasturlash.uz.dto.comment.CommentDTO;
import dasturlash.uz.dto.comment.CommentLikeResponseDTO;
import dasturlash.uz.dto.comment.CommentShortInfo;
import dasturlash.uz.dto.filter.FilterCommentDTO;
import dasturlash.uz.dto.filter.FilterResultDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Repository
public class CommentCustomRepository {

    @Autowired
    private EntityManager entityManager;

    public FilterResultDTO<Object[]> filter(FilterCommentDTO dto, int page, int size) {
        String selectQuery = """
                select 
                    c.id as id,
                    c.content as content,
                    c.createDate as createDate,
                    c.updateDate as updateDate,
                    p.id as profileId as profileId,
                    p.name as profileName
                    c.replyId as replyId,
                    a.id as articleId,
                    a.title as articleTitle,
                    c.visible as visible,
                    count(cl.emotion, 'LIKE') as likeCount,
                    count(cl.emotion, 'DISLIKE') as dislikeCount
                from CommentEntity as c
                inner join ProfileEntity as p on c.profileId = p.id
                inner join ArticleEntity as a on c.articleId = a.id
                left join CommentLikeEntity as cl on cl.commentId = c.id
                """;
        String countQuery = """
                select count(c
                from CommentEntity as c
                inner join ProfileEntity as p on c.profileId = p.id
                inner join ArticleEntity as a on c.articleId = a.id
                left join CommentLikeEntity as cl on cl.commentId = c.id
                """;

        StringBuilder conditions = new StringBuilder(" where 1=1 ");

        Map<String, Object> parameters = new java.util.HashMap<>();
        if (dto.getArticleId() != null) {
            conditions.append(" and c.articleId = :articleId ");
            parameters.put("articleId", dto.getArticleId());
        }
        if (dto.getProfileId() != null) {
            conditions.append(" and c.profileId = :profileId ");
            parameters.put("profileId", dto.getProfileId());
        }
        if (dto.getCreatedDateTo() != null){
            LocalDateTime temp = LocalDateTime.of(dto.getCreatedDateTo(), LocalTime.MAX);
            conditions.append(" and c.createDate <= :createdDateTo ");
            parameters.put("createdDateTo", temp);
        }
        if (dto.getCreatedDateFrom() != null){
            LocalDateTime temp = LocalDateTime.of(dto.getCreatedDateFrom(), LocalTime.MIN);
            conditions.append(" and c.createDate >= :createdDateFrom ");
            parameters.put("createdDateFrom", temp);
        }
        String select = selectQuery + conditions + " group by c.id ";
        String count = countQuery + conditions;

        Query selectQ = entityManager.createQuery(select);
        parameters.forEach(selectQ::setParameter);

        selectQ.setFirstResult(page*size);
        selectQ.setMaxResults(size);

        List<Object[]> resultList = selectQ.getResultList();

        Query countQ = entityManager.createQuery(count);
        parameters.forEach(countQ::setParameter);

        Long totalCount = (Long) countQ.getSingleResult();

        return new FilterResultDTO<>(resultList, totalCount);
    }
}
