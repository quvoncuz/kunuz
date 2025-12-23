package dasturlash.uz.controller.comment;

import dasturlash.uz.dto.comment.CommentLikeDTO;
import dasturlash.uz.service.comment.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentLikeController {

    @Autowired
    private CommentLikeService commentLikeService;

    @PostMapping("/like")
    public ResponseEntity<?> like(@RequestBody CommentLikeDTO dto,
                                  @RequestHeader(value = "Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(commentLikeService.like(dto, token));
    }

    @PostMapping("/dislike")
    public ResponseEntity<?> dislike(@RequestBody CommentLikeDTO dto,
                                     @RequestHeader(value = "Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(commentLikeService.dislike(dto, token));
    }

}
