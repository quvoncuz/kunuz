package dasturlash.uz.controller;

import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.dto.ProfileInfoDTO;
import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("")
    public ResponseEntity<ProfileInfoDTO> create(@Valid @RequestBody ProfileDTO profileDTO) {
        return ResponseEntity.ok(profileService.create(profileDTO));
    }

    @GetMapping("{id}")
    public ResponseEntity<ProfileDTO> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(profileService.getById(id));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<ProfileDTO> updateByAdmin(@PathVariable("id") Integer id,
                                                    @Valid @RequestBody ProfileDTO profileDTO) {
        return ResponseEntity.ok(profileService.updateByAdmin(id, profileDTO));
    }

    @PutMapping("{id}")
    public ResponseEntity<ProfileInfoDTO> update(@PathVariable("id") Integer id,
                                                 @Valid @RequestBody ProfileInfoDTO profileDTO) {
        return ResponseEntity.ok(profileService.update(id, profileDTO));
    }

    @GetMapping("/all")
    public ResponseEntity<PageImpl<ProfileDTO>> getAll(@RequestParam int page,
                                                       @RequestParam int size) {
        return ResponseEntity.ok(profileService.pagination(page - 1, size));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(profileService.deleteById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Boolean> updatePassword(@PathVariable("id") Integer id, @RequestParam String oldPassword, @RequestParam String newPassword) {
        return ResponseEntity.ok(profileService.updatePassword(id, oldPassword, newPassword));
    }
}
