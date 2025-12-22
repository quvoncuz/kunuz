package dasturlash.uz.service.article;

import dasturlash.uz.dto.JwtDTO;
import dasturlash.uz.dto.article.ArticleLikeDTO;
import dasturlash.uz.dto.article.ArticleLikeResponseDTO;
import dasturlash.uz.entity.article.ArticleLikeEntity;
import dasturlash.uz.enums.Like;
import dasturlash.uz.repository.article.ArticleLikeRepository;
import dasturlash.uz.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ArticleLikeService {

    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    public ArticleLikeResponseDTO addReaction(ArticleLikeDTO dto, String token) {
        Integer profileId = JwtUtil.decode(token).getId();
        Optional<ArticleLikeEntity> articleLike = articleLikeRepository.findArticleLikeEntityByProfile_IdAndArticleId(profileId, dto.getArticleId());
        if (articleLike.isPresent()) {
            ArticleLikeEntity like = articleLike.get();
            if (like.getEmotion().equals(dto.getEmotion())) {
                articleLikeRepository.deleteByArticleIdAndProfileId(dto.getArticleId(), profileId);
                return buildResponse("Emotion removed", dto.getArticleId(), dto.getEmotion());
            } else {
                like.setEmotion(dto.getEmotion());
                like.setCreatedDate(LocalDateTime.now());
                articleLikeRepository.save(like);
                return buildResponse("Emotion changed", dto.getArticleId(), dto.getEmotion());
            }
        } else {
            ArticleLikeEntity like = new ArticleLikeEntity();
            like.setArticleId(dto.getArticleId());
            like.setProfileId(profileId);
            like.setEmotion(dto.getEmotion());
            articleLikeRepository.save(like);
            return buildResponse("Emotion created", dto.getArticleId(), dto.getEmotion());
        }
    }

    private ArticleLikeResponseDTO buildResponse(String message, String articleId, Like emotion){
        ArticleLikeResponseDTO allDetailByArticleId = articleLikeRepository.findAllDetailByArticleId(articleId);
        allDetailByArticleId.setMessage(message);
        allDetailByArticleId.setUserEmotion(emotion);
        return allDetailByArticleId;
    }

    public ArticleLikeResponseDTO getStats(String articleId) {
        return articleLikeRepository.findAllDetailByArticleId(articleId);
    }
}
