package dasturlash.uz.repository.comment;

import dasturlash.uz.dto.comment.CommentShortInfo;
import dasturlash.uz.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {

    Optional<CommentEntity> findByIdAndVisibleIsTrue(String commentId);

    @Query("""
            select c.id as id,
                   c.createdDate as createdDate,
                   c.updatedDate as updatedDate,
                   c.profile.id as profileId,
                   c.profile.name as profileName,
                   c.profile.surname as profileSurname,
                   c.profile.photoId as profileImageId,
                   at.path as profileImageUrl
                        from CommentEntity as c
                        left join AttachEntity as at on c.profile.photoId = at.id
                        where c.visible = true 
                        and c.replyId = ?1
            """)
    List<CommentShortInfo> findAllByReplyId(String commentId);

    @Query("""
            select c.id as id,
                   c.createdDate as createdDate,
                   c.updatedDate as updatedDate,
                   c.profile.id as profileId,
                   c.profile.name as profileName,
                   c.profile.surname as profileSurname,
                   c.profile.photoId as profileImageId,
                   at.path as profileImageUrl,
                   count(case when cl.emotion = 'LIKE' then 1 end) as likeCount,
                   count(case when cl.emotion = 'DISLIKE' then 1 end) as dislikeCount
                        from CommentEntity as c
                        left join AttachEntity as at on c.profile.photoId = at.id
                        left join CommentLikeEntity as cl on cl.commentId = c.id
                        where c.visible = true 
                        and c.articleId = ?1
                        group by c.id
            """)
    List<CommentShortInfo> findAllByArticleId(String articleId);
}
