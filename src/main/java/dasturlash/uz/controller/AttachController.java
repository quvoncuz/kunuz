package dasturlash.uz.controller;

import dasturlash.uz.dto.AttachDTO;
import dasturlash.uz.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;

    @PostMapping("")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachService.upload(file));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> open(@PathVariable("id") String id){
        return attachService.open(id);
    }

    @GetMapping("download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") String id){
        return attachService.download(id);
    }

    @GetMapping("")
    public Page<AttachDTO> pagination(@RequestParam int page,
                                      @RequestParam int size) {
        return attachService.pagination(page-1, size);
    }
}
