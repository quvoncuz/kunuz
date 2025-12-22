package dasturlash.uz.controller;

import dasturlash.uz.dto.comment.CommentLikeDTO;
import dasturlash.uz.service.comment.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment/like")
public class CommentLikeController {

    @Autowired
    private CommentLikeService commentLikeService;

    public ResponseEntity<?> like(@RequestBody CommentLikeDTO dto,
                                  @RequestHeader(value = "Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(commentLikeService.like(dto, token));
    }

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
