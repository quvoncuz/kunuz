package dasturlash.uz.service.comment;

import dasturlash.uz.dto.comment.CommentLikeDTO;
import dasturlash.uz.dto.comment.CommentLikeResponseDTO;
import dasturlash.uz.entity.CommentLikeEntity;
import dasturlash.uz.enums.Like;
import dasturlash.uz.repository.comment.CommentLikeRepository;
import dasturlash.uz.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentLikeService {
    @Autowired
    private CommentLikeRepository commentLikeRepository;

    public CommentLikeResponseDTO like(CommentLikeDTO dto, String token) {
        Integer profileId = JwtUtil.decode(token).getId();
        Optional<CommentLikeEntity> byCommentIdAndProfileId = commentLikeRepository.findByCommentIdAndProfileId(dto.getCommentId(), profileId);
        if (byCommentIdAndProfileId.isPresent()){
            CommentLikeEntity like = byCommentIdAndProfileId.get();
            if (like.getEmotion().toString().equals("LIKE")){
                commentLikeRepository.delete(like);
                return buildResponse("Like removed", dto.getCommentId(), null);
            } else {
                like.setEmotion(Like.LIKE);
                commentLikeRepository.save(like);
                return buildResponse("Changed to Like", dto.getCommentId(), Like.LIKE);
            }
        } else {
            CommentLikeEntity like = new CommentLikeEntity();
            like.setCommentId(dto.getCommentId());
            like.setProfileId(profileId);
            like.setEmotion(Like.LIKE);
            commentLikeRepository.save(like);
            return buildResponse("Liked", dto.getCommentId(), Like.LIKE);
        }
    }

    public CommentLikeResponseDTO dislike(CommentLikeDTO dto, String token) {
        Integer profileId = JwtUtil.decode(token).getId();
        Optional<CommentLikeEntity> byCommentIdAndProfileId = commentLikeRepository.findByCommentIdAndProfileId(dto.getCommentId(), profileId);
        if (byCommentIdAndProfileId.isPresent()){
            CommentLikeEntity like = byCommentIdAndProfileId.get();
            if (like.getEmotion().toString().equals("DISLIKE")){
                commentLikeRepository.delete(like);
                return buildResponse("Dislike removed", dto.getCommentId(), null);
            } else {
                like.setEmotion(Like.DISLIKE);
                commentLikeRepository.save(like);
                return buildResponse("Changed to Dislike", dto.getCommentId(), Like.DISLIKE);
            }
        } else {
            CommentLikeEntity like = new CommentLikeEntity();
            like.setCommentId(dto.getCommentId());
            like.setProfileId(profileId);
            like.setEmotion(Like.DISLIKE);
            commentLikeRepository.save(like);
            return buildResponse("Disliked", dto.getCommentId(), Like.DISLIKE);
        }
    }

//    public CommentLikeResponseDTO getStats(String commentId){
//        return commentLikeRepository.findAllDetailByCommentId(commentId);
//    }

    private CommentLikeResponseDTO buildResponse(String message, String commentId, Like emotion){
        CommentLikeResponseDTO responseDTO = commentLikeRepository.findAllDetailByCommentId(commentId);
        responseDTO.setMessage(message);
        responseDTO.setUserEmotion(emotion);
        return responseDTO;
    }
}
