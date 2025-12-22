package dasturlash.uz.controller.article;

import dasturlash.uz.dto.article.ArticleLikeDTO;
import dasturlash.uz.enums.Like;
import dasturlash.uz.service.article.ArticleLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
@PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'PUBLISHER')")
public class ArticleLikeController {
    @Autowired
    private ArticleLikeService articleLikeService;

    @PostMapping("/{articleId}/like")
    public ResponseEntity<?> like(@PathVariable String articleId,
                                   @RequestHeader(value = "Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token not found");
        }

        String token = authHeader.substring(7);

        return ResponseEntity.ok(articleLikeService.addReaction(new ArticleLikeDTO(articleId, Like.LIKE), token));

    }

    @PostMapping("/{articleId}/dislike")
    public ResponseEntity<?> dislike(@PathVariable String articleId,
                                      @RequestHeader(value = "Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token not found");
        }

        String token = authHeader.substring(7);
        return ResponseEntity.ok(articleLikeService.addReaction(new ArticleLikeDTO(articleId, Like.DISLIKE), token));
    }

    @GetMapping("/{articleId}/stats")
    public ResponseEntity<?> getStats(@PathVariable String articleId){
        return ResponseEntity.ok(articleLikeService.getStats(articleId));
    }
}
