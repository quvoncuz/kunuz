package dasturlash.uz.controller.comment;

import dasturlash.uz.dto.comment.CommentDTO;
import dasturlash.uz.dto.filter.FilterCommentDTO;
import dasturlash.uz.service.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody CommentDTO dto) {
        return ResponseEntity.ok(commentService.mergeComment(dto, null));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> update(@PathVariable String commentId, @RequestBody CommentDTO dto) {
        return ResponseEntity.ok(commentService.mergeComment(dto, commentId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> delete(@PathVariable String commentId,
                                    @RequestHeader(value = "Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
            commentService.delete(commentId, token);
            return ResponseEntity.ok("Comment deleted successfully");
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> filter(@RequestBody FilterCommentDTO filterDTO, int page, int size) {
        return ResponseEntity.ok(commentService.filter(filterDTO, page, size));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/replies/{commentId}")
    public ResponseEntity<?> getAllByArticleId(@PathVariable String commentId) {
        return ResponseEntity.ok(commentService.getRepliedCommentListByCommentId(commentId));
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<?> getCommentListByArticleId(@PathVariable String articleId){
        return ResponseEntity.ok(commentService.getCommentListByArticleId(articleId));
    }
}
