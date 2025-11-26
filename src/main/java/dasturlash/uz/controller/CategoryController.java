package dasturlash.uz.controller;

import dasturlash.uz.dto.CategoryDTO;
import dasturlash.uz.entity.CategoryEntity;
import dasturlash.uz.service.CategoryService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.parser.AcceptLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.create(categoryDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Integer id,
                                              @Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.update(id, categoryDTO));
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryEntity>> findAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(categoryService.delete(id));
    }

    @GetMapping("/lang")
    public ResponseEntity<List<CategoryDTO>> findByLang(@RequestHeader(name = "Accept-Language", defaultValue = "UZ") String lang) {
        return ResponseEntity.ok(categoryService.getAllByLang(lang));
    }



}
