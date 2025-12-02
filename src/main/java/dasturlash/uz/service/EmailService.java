package dasturlash.uz.service;

import dasturlash.uz.dto.EmailDTO;
import dasturlash.uz.entity.EmailEntity;
import dasturlash.uz.enums.SmsStatus;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private EmailRepository emailRepository;

    public EmailDTO create(EmailDTO dto) {
        EmailEntity emailEntity = new EmailEntity(dto.getCode(), dto.getEmail(), dto.getBody(), SmsStatus.SENT);
        emailEntity = emailRepository.save(emailEntity);
        dto.setId(emailEntity.getId());
        return dto;
    }

    public List<EmailDTO> findAll() {
        List<EmailEntity> emailEntities = emailRepository.findAll();
        List<EmailDTO> emailList = new ArrayList<>();
        for (EmailEntity emailEntity : emailEntities) {
            emailList.add(toDTO(emailEntity));
        }
        return emailList;
    }

    public EmailDTO findById(Integer id) {
        return toDTO(emailRepository.findById(id).orElseThrow(() -> new AppBadException("Mail not found")));
    }

    public List<EmailDTO> findByMail(String email) {
        List<EmailEntity> emailEntities = emailRepository.findAllByEmail(email);
        List<EmailDTO> emailList = new ArrayList<>();
        for (EmailEntity entity : emailEntities) {
            emailList.add(toDTO(entity));
        }
        return emailList;
    }

    public List<EmailDTO> findByDate(String date) {
        LocalDateTime start = LocalDate.parse(date).atStartOfDay();
        LocalDateTime end = LocalDate.parse(date).atTime(LocalTime.MAX);
        List<EmailEntity> emailEntities = emailRepository.findAllByCreatedDateBetween(start, end);
        List<EmailDTO> emailList = new ArrayList<>();
        for (EmailEntity entity : emailEntities) {
            emailList.add(toDTO(entity));
        }
        return emailList;
    }

    public PageImpl<EmailEntity> pagination(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<EmailEntity> emailEntities = emailRepository.findAll(pageRequest);
        List<EmailEntity> resultList = emailEntities.getContent();

        return new PageImpl<>(resultList, pageRequest, emailEntities.getTotalElements());
    }

    private EmailDTO toDTO(EmailEntity emailEntity) {
        EmailDTO dto = new EmailDTO();
        dto.setId(emailEntity.getId());
        dto.setEmail(emailEntity.getEmail());
        dto.setBody(emailEntity.getBody());
        dto.setCode(emailEntity.getCode());
        return dto;
    }
}
