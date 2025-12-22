package dasturlash.uz.controller.article;

import dasturlash.uz.dto.article.ArticleChangeStatusDTO;
import dasturlash.uz.dto.article.ArticleDTO;
import dasturlash.uz.dto.article.ArticleInfoDTO;
import dasturlash.uz.dto.filter.AdminFilterDTO;
import dasturlash.uz.dto.filter.FilterDTO;
import dasturlash.uz.service.article.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/moderator")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<ArticleInfoDTO> create(@RequestBody ArticleDTO dto) {
        return ResponseEntity.ok(articleService.create(dto));
    }

    @PutMapping("/moderator/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<ArticleInfoDTO> update(@RequestBody ArticleDTO dto,
                                                 @PathVariable String id) {
        return ResponseEntity.ok(articleService.update(id, dto));
    }

    @DeleteMapping("/publisher/{id}")
    @PreAuthorize("hasRole('PUBLISHER')")
    public ResponseEntity<String> delete(@PathVariable String id) {
        return ResponseEntity.ok(articleService.delete(id));
    }

    @PutMapping("/publisher/update")
    @PreAuthorize("hasRole('PUBLISHER')")
    public ResponseEntity<String> changeStatus(@RequestBody ArticleChangeStatusDTO dto) {
        return ResponseEntity.ok(articleService.changeStatus(dto.getArticleId(), dto.getStatus()));
    }

    @GetMapping("/last")
    public PageImpl<ArticleDTO> getLast12PublishedArticleExceptGivenIds(@RequestParam List<Integer> ids) {
        return articleService.getLast12PublishedArticleExceptGivenIds(ids);
    }

    @GetMapping("/last/section")
    public PageImpl<ArticleDTO> getLastNArticleBySectionId(@RequestParam Integer sectionId,
                                                           @RequestParam int page,
                                                           @RequestParam int size) {
        return articleService.getLastNArticleBySectionId(sectionId, page, size);
    }

    @GetMapping("/last/category")
    public PageImpl<ArticleDTO> getLastNArticleByCategoryId(@RequestParam Integer categoryId,
                                                            @RequestParam int page,
                                                            @RequestParam int size) {
        return articleService.getLastNArticleByCategoryId(categoryId, page, size);
    }

    @GetMapping("/last/region")
    public PageImpl<ArticleDTO> getLastNArticleByRegionId(@RequestParam Integer categoryId,
                                                          @RequestParam int page,
                                                          @RequestParam int size) {
        return articleService.getLastNArticleByRegionId(categoryId, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleByIdAndLang(@PathVariable String id,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String lang) {
        return ResponseEntity.ok(articleService.getArticleByIdAndLang(id, lang));
    }

//    @GetMapping("/search")
//    public PageImpl<ArticleDTO> getLastNArticleByTagName(@RequestParam String tagName,
//                                                @RequestParam int page,
//                                                @RequestParam int size){
//        return articleService.getLastNArticleByTagName(tagName, page, size);
//    }

    @GetMapping("/last/except")
    public ResponseEntity<List<ArticleDTO>> getLast4ArticleBySectionIdExceptOneArticleId(@RequestParam String articleId,
                                                                                         @RequestParam Integer sectionId) {
        return ResponseEntity.ok(articleService.getLast4ArticleBySectionIdExceptOneArticleId(articleId, sectionId));
    }

    @GetMapping("/last/most/viewed")
    public ResponseEntity<List<ArticleDTO>> getLast4MostViewedArticleExceptOneArticleId(@RequestParam String articleId) {
        return ResponseEntity.ok(articleService.getLast4MostViewedArticleExceptOneArticleId(articleId));
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<Long> increaseShareCount(@PathVariable("id") String articleId) {

        return ResponseEntity.ok(articleService.increaseShareCount(articleId));
    }

    @PostMapping("/filter")
    public ResponseEntity<PageImpl<ArticleDTO>> publicFilter(@RequestBody FilterDTO dto,
                                             @RequestParam int page,
                                             @RequestParam int size) {
        return ResponseEntity.ok(articleService.filter(dto, page-1, size, false));
    }

    @PostMapping("/moderator/filter")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<PageImpl<ArticleDTO>> moderatorFilter(@RequestBody FilterDTO dto,
                                                @RequestParam int page,
                                                @RequestParam int size) {
        return ResponseEntity.ok(articleService.filter(dto, page-1, size, true));
    }

    @PostMapping("/admin/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageImpl<ArticleDTO>> adminFilter(@RequestBody AdminFilterDTO dto,
                                                   @RequestParam int page,
                                                   @RequestParam int size) {
        return ResponseEntity.ok(articleService.adminFilter(dto, page-1, size));
    }
}
