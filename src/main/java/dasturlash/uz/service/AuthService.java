package dasturlash.uz.service;

import dasturlash.uz.dto.AuthorizationDTO;
import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.dto.RegistrationDTO;
import dasturlash.uz.entity.EmailEntity;
import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.SmsStatus;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.EmailRepository;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.repository.SmsRepository;
import dasturlash.uz.util.SmsCodeGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService implements SmsCodeGenerator {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ProfileRoleService profileRoleService;
    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private EmailSendingService emailSendingService;

    public String registration(RegistrationDTO dto) {
        // check
        Optional<ProfileEntity> existOptional = profileRepository.findByUsernameAndVisibleIsTrue(dto.getUsername());
        if (existOptional.isPresent()) {
            throw new AppBadException("Username already exists");
        }
        // create profile
        ProfileEntity profile = new ProfileEntity();
        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
        profile.setUsername(dto.getUsername());
        profile.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        profile.setStatus(Status.NOT_ACTIVE);
        profileRepository.save(profile);
        // create profile roles
        profileRoleService.create(profile.getId(), Role.ROLE_USER);
        // send sms to amail or phone
        StringBuilder content = new StringBuilder("Sms code to confirm: ");
        StringBuilder sms = new StringBuilder(generateSmsCode());

        EmailEntity email = new EmailEntity(sms.toString(), profile.getUsername(), SmsStatus.SENT);

        content.append(sms);

        emailRepository.save(email);
        emailSendingService.sendSimpleMessage(dto.getUsername(), "Complete Registration", content.toString());


        // response
        return "Tastiqlash kodi ketdi mazgi qara.";
    }

    public ProfileDTO login(@Valid AuthorizationDTO dto) {
        Optional<ProfileEntity> profileOptional = profileRepository.findByUsernameAndVisibleIsTrue(dto.getUsername());
        if (profileOptional.isEmpty()) {
            throw new AppBadException("Username or password wrong");
        }
        if (profileOptional.get().getStatus() == Status.BLOCKED){
            throw new AppBadException("This user is blocked. Please contact administrator");
        }
        ProfileEntity entity = profileOptional.get();
        if (!bCryptPasswordEncoder.matches(dto.getPassword(), entity.getPassword())) {
            throw new AppBadException("Username or password wrong");
        }
        if (!entity.getStatus().equals(Status.ACTIVE)) {
            throw new AppBadException("User in wrong status");
        }
        // status
        ProfileDTO response = new ProfileDTO();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setSurname(entity.getSurname());
        response.setUsername(entity.getUsername());
        response.setRoles(profileRoleService.getByProfileId(entity.getId()));
        return response;
    }

    public Boolean confirmByEmail(String username, String code) {
        Optional<EmailEntity> entity = emailRepository.findByUsername(username);
        if (entity.isEmpty()) {
            throw new AppBadException("Username or code wrong");
        }
        if (!entity.get().getCode().equals(code)) {
            throw new AppBadException("Wrong code");
        }
        if (entity.get().getCreatedDate().plusMinutes(3).isBefore(LocalDateTime.now())) {
            throw new AppBadException("Expired code");
        }

        emailRepository.updateStatus(entity.get().getId(), SmsStatus.USED);
        profileRepository.confirm(username, Status.ACTIVE);

        return true;
    }


}
