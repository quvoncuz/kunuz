package dasturlash.uz.repository.comment;

import dasturlash.uz.dto.comment.CommentLikeResponseDTO;
import dasturlash.uz.entity.CommentLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, String> {
    @Query("""
            select cl from CommentLikeEntity as cl 
                where cl.commentId = ?1 and 
                      cl.profileId = ?2
            """)
    Optional<CommentLikeEntity> findByCommentIdAndProfileId(String commentId, Integer profileId);

    @Query("""
            select '',
                   a.commentId,
                   count(case when a.emotion = 'LIKE' then 1 end),
                   count(case when a.emotion = 'DISLIKE' then 1 end ),
                   ''
            from CommentLikeEntity as a
            where a.commentId = ?1
            group by a.commentId
            """)
    CommentLikeResponseDTO findAllDetailByCommentId(String commentId);
}
