package dasturlash.uz.controller;

import dasturlash.uz.dto.AuthorizationDTO;
import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.dto.RegistrationDTO;
import dasturlash.uz.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationDTO dto) {
        return ResponseEntity.ok(authService.registration(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody AuthorizationDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping("/varification")
    public ResponseEntity<?> varification(@RequestParam String username,
                                          @RequestParam String code) {
        return ResponseEntity.ok(authService.confirmByEmail(username, code));
    }


}

