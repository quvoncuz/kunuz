package dasturlash.uz.controller;

import dasturlash.uz.entity.SmsEntity;
import dasturlash.uz.service.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sms")
@PreAuthorize("hasRole('ADMIN')")
public class SmsController {
    @Autowired
    private SmsService smsService;

    @GetMapping("")
    public ResponseEntity<List<?>> getAllByUsername(@RequestParam String phone) {
        return ResponseEntity.ok(smsService.getByPhone(phone));
    }

    @GetMapping("/sms")
    public ResponseEntity<PageImpl<SmsEntity>> getAllSms(int page, int size) {
        return ResponseEntity.ok(smsService.pagination(page-1, size));
    }

    @GetMapping("/")
    public ResponseEntity<List<SmsEntity>> getAllByDate(@RequestParam String date) {
        return ResponseEntity.ok(smsService.getByGivenDate(date));
    }
}
