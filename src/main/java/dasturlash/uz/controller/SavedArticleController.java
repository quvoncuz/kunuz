package dasturlash.uz.controller;

import dasturlash.uz.service.SavedArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saved-articles")
@PreAuthorize("hasRole('USER')")
public class SavedArticleController {

    @Autowired
    private SavedArticleService savedArticleService;

    @PostMapping("/{articleId}")
    public ResponseEntity<?> create(@PathVariable String articleId,
                                    @RequestHeader(value = "Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(savedArticleService.create(articleId, token));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getAllByProfileId(@RequestHeader(value = "Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        return ResponseEntity.ok(savedArticleService.getAllByProfileId(token));
    }
}
