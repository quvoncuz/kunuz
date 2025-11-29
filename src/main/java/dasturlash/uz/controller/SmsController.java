package dasturlash.uz.controller;

import dasturlash.uz.entity.EmailEntity;
import dasturlash.uz.entity.SmsEntity;
import dasturlash.uz.service.SmsAndEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sms")
public class SmsController {
    @Autowired
    private SmsAndEmailService smsAndEmailService;

    @GetMapping("")
    public ResponseEntity<List<?>> getAllByUsername(@RequestParam String username) {
        return ResponseEntity.ok(smsAndEmailService.getByUsername(username));
    }

    @GetMapping("/sms")
    public ResponseEntity<List<SmsEntity>> getAllSms() {
        return ResponseEntity.ok(smsAndEmailService.findAllSms());
    }

    @GetMapping("/email")
    public ResponseEntity<List<EmailEntity>> getAllEmail() {
        return ResponseEntity.ok(smsAndEmailService.findAllEmail());
    }
}
