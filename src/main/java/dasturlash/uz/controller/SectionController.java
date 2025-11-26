package dasturlash.uz.controller;

import dasturlash.uz.dto.SectionDTO;
import dasturlash.uz.entity.SectionEntity;
import dasturlash.uz.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/section")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @PostMapping("")
    public ResponseEntity<SectionDTO> create(@RequestBody SectionDTO sectionDTO) {
        return ResponseEntity.ok(sectionService.create(sectionDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectionDTO> update(@PathVariable("id") Integer id,
                                             @RequestBody SectionDTO sectionDTO) {
        return ResponseEntity.ok(sectionService.update(id, sectionDTO));
    }


    @GetMapping("")
    public ResponseEntity<PageImpl<SectionDTO>> getAllSections(@RequestHeader(name = "Accept-Language", defaultValue = "UZ") String lang,
                                                               @RequestParam int page,
                                                               @RequestParam int size) {
        return ResponseEntity.ok(sectionService.getAllByLang(lang, page-1, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(sectionService.deleteById(id));
    }

    @GetMapping("/admin")
    public ResponseEntity<PageImpl<SectionEntity>> getAllByPagination(@RequestParam int page,
                                                                      @RequestParam int size) {
        return ResponseEntity.ok(sectionService.pagination(page-1, size));
    }
}
