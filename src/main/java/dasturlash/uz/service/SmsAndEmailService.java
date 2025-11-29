package dasturlash.uz.service;

import dasturlash.uz.entity.EmailEntity;
import dasturlash.uz.entity.SmsEntity;
import dasturlash.uz.repository.EmailRepository;
import dasturlash.uz.repository.SmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsAndEmailService {
    @Autowired
    private SmsRepository smsRepository;
    @Autowired
    private EmailRepository emailRepository;

    public List<?> getByUsername(String username) {
        List<SmsEntity> smsList = smsRepository.findAllByPhone(username);
        List<EmailEntity> emailList = emailRepository.findAllByEmail(username);
        if (smsList.isEmpty() && emailList.isEmpty()) {
            throw new RuntimeException("No sms or email found");
        }
        if (smsList.isEmpty()) {
            return emailList;
        } else {
            return smsList;
        }
    }

    public List<SmsEntity> findAllSms() {
        return smsRepository.findAll();
    }
    public List<EmailEntity> findAllEmail() {
        return emailRepository.findAll();
    }
}
