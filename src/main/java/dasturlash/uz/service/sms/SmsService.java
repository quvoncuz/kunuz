package dasturlash.uz.service.sms;

import dasturlash.uz.entity.SmsEntity;
import dasturlash.uz.enums.SmsStatus;
import dasturlash.uz.repository.SmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class SmsService {
    @Autowired
    private SmsRepository smsRepository;

    public SmsEntity create(String phone, String code, String body) {
        SmsEntity smsEntity = new SmsEntity(phone, code, body);
        smsEntity.setSmsStatus(SmsStatus.SENT);
        return smsRepository.save(smsEntity);
    }

    public List<SmsEntity> getByPhone(String phone) {
        return smsRepository.findAllByPhone(phone);
    }

    public List<SmsEntity> getByGivenDate(String date) {
        LocalDateTime start = LocalDate.parse(date).atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);

        return smsRepository.findAllByCreatedDateBetween(start, end);
    }

    public PageImpl<SmsEntity> pagination(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<SmsEntity> smsEntities = smsRepository.findAll(pageRequest);
        List<SmsEntity> resultList = smsEntities.getContent();
        return new PageImpl(resultList, pageRequest, smsEntities.getTotalElements());
    }
}
