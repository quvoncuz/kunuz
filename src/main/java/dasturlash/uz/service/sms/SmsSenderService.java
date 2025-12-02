package dasturlash.uz.service.sms;

import dasturlash.uz.dto.sms.SmsProviderTokenDTO;
import dasturlash.uz.dto.sms.SmsRequestDTO;
import dasturlash.uz.dto.sms.SmsTokenProviderResponse;
import dasturlash.uz.entity.SmsEntity;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.SmsRepository;
import dasturlash.uz.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class SmsSenderService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${sms.eskiz.email}")
    private String email;
    @Value("${sms.eskiz.password}")
    private String password;
    private final String url = "https://notify.eskiz.uz/api/";
    private ResponseEntity<SmsTokenProviderResponse> response;

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsRepository smsRepository;

    public void sendRegistrationSMS(String phone) {
        String smsCode = RandomUtil.fiveDigit()+"";
        System.out.println("SMS CODE: " + smsCode);
        SmsEntity entity = smsRepository.findByUsername(phone).orElse(null);
        if (entity != null) {
            if(entity.getCreatedDate().plusMinutes(4).isAfter(LocalDateTime.now())){
                throw new AppBadException("Please, try again");
            }
        }
//        String body = "<#>bormi.uz chegirmalar do'konidan ro'yxatdan o'tish uchun tasdiqlash kodi: " + smsCode; // test message
        String body = "Bu Eskiz dan test"; // test message
        try {
            sendSms(phone, body);
            smsService.create(phone, smsCode, body);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppBadException("Something went wrong");
        }
    }

    private void sendSms(String phone, String body) {
        SmsRequestDTO smsRequestDTO = new SmsRequestDTO();
        smsRequestDTO.setMobile_phone("+" + phone);
        smsRequestDTO.setMessage(body);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + getToken());

        RequestEntity<SmsRequestDTO> request = RequestEntity
                .post(url + "message/sms/send")
                .headers(headers)
                .body(smsRequestDTO);

        restTemplate.exchange(request, String.class);
    }


    private String getToken() {
        if (response == null || response.getBody().getCreatedDate().plusDays(30).isBefore(LocalDate.now())) {
            SmsProviderTokenDTO smsProviderTokenDTO = new SmsProviderTokenDTO();
            smsProviderTokenDTO.setEmail(email);
            smsProviderTokenDTO.setPassword(password);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            RequestEntity<SmsProviderTokenDTO> request = RequestEntity
                    .post(url + "auth/login")
                    .headers(headers)
                    .body(smsProviderTokenDTO);

            response = restTemplate.exchange(request, SmsTokenProviderResponse.class);
            response.getBody().setCreatedDate(LocalDate.now());
        }
        return response.getBody().getData().getToken();
    }

}
