package dasturlash.uz.service;

import dasturlash.uz.dto.auth.AuthorizationDTO;
import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.dto.auth.RegistrationDTO;
import dasturlash.uz.dto.auth.VerificationDTO;
import dasturlash.uz.entity.EmailEntity;
import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.entity.SmsEntity;
import dasturlash.uz.enums.Role;
import dasturlash.uz.enums.SmsStatus;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.EmailRepository;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.repository.SmsRepository;
import dasturlash.uz.service.sms.SmsSenderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {
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
    private SmsSenderService smsSender;

    @Autowired
    private EmailSendingService emailSendingService;

    public String registration(RegistrationDTO dto) {
        // check
        Optional<ProfileEntity> existOptional = profileRepository.findByUsernameAndVisibleIsTrue(dto.getUsername());
        if (existOptional.isPresent()) {
            ProfileEntity existsProfile = existOptional.get();
            if (existsProfile.getStatus() == Status.NOT_ACTIVE) {
                profileRoleService.deleteRolesByProfileId(existsProfile.getId());
                profileRepository.deleteById(existsProfile.getId()); // delete
            } else {
                throw new AppBadException("Username already exists");
            }

        }
        // create profile
        ProfileEntity profile = new ProfileEntity();
        profile.setName(dto.getName());
        if (!dto.getUsername().contains("@")) {
            profile.setUsername(normalizePhone(dto.getUsername()));
        } else {
            profile.setUsername(dto.getUsername());
        }
        profile.setSurname(dto.getSurname());
        profile.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        profile.setStatus(Status.NOT_ACTIVE);
        profileRepository.save(profile);
        // create profile roles
        profileRoleService.create(profile.getId(), Role.ROLE_USER);
        // send sms to amail or phone

        if (dto.getUsername().contains("@")) {
            emailSendingService.sendRegistrationStyledEmail(dto.getName(), dto.getUsername());
        } else {
            smsSender.sendRegistrationSMS(normalizePhone(dto.getUsername()));
        }


        // response
        return "Tastiqlash kodi ketdi mazgi qara.";
    }

    public ProfileDTO login(@Valid AuthorizationDTO dto) {
        Optional<ProfileEntity> profileOptional = profileRepository.findByUsernameAndVisibleIsTrue(dto.getUsername());
        if (profileOptional.isEmpty()) {
            throw new AppBadException("Username or password wrong");
        }
        if (profileOptional.get().getStatus() == Status.BLOCKED) {
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

    public Boolean verification(VerificationDTO dto) {
        boolean isEmail = dto.getUsername().contains("@");
        if (isEmail) {
            return verificationByEmail(dto);
        } else {
            return verificationByPhone(dto);
        }
    }

    private Boolean verificationByEmail(VerificationDTO dto) {
        ProfileEntity profile = profileRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new AppBadException("User not found"));

        if (!profile.getStatus().equals(Status.NOT_ACTIVE)) {
            throw new AppBadException("User is on wrong status");
        }

        EmailEntity email = emailRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new AppBadException("*Username or code wrong"));
        if (!email.getCode().equals(dto.getCode())) {
            throw new AppBadException("Username or *code wrong");
        }

        if (email.getCreatedDate().plusMinutes(5).isBefore(LocalDateTime.now())) {
            throw new AppBadException("Expired code");
        }
        emailRepository.updateStatus(profile.getId(), SmsStatus.USED);

        profile.setStatus(Status.ACTIVE);
        profileRepository.save(profile);
        return true;
    }

    private Boolean verificationByPhone(VerificationDTO dto) {
        String phone = normalizePhone(dto.getUsername());

        ProfileEntity profile = profileRepository.findByUsername(phone)
                .orElseThrow(() -> new AppBadException("User not found"));

        if (!profile.getStatus().equals(Status.NOT_ACTIVE)) {
            throw new AppBadException("User is on wrong status");
        }

        SmsEntity sms = smsRepository.findByUsername(phone)
                .orElseThrow(() -> new AppBadException("*Username or code wrong"));

        if (!sms.getCode().equals(dto.getCode())) {
            throw new AppBadException("Username or *code wrong");
        }

        if (sms.getCreatedDate().plusMinutes(5).isBefore(LocalDateTime.now())) {
            throw new AppBadException("Expired code");
        }

        smsRepository.updateStatus(profile.getId(), SmsStatus.USED);

        profile.setStatus(Status.ACTIVE);
        profileRepository.save(profile);
        return true;
    }


    private String normalizePhone(String phone) {
        String digits = phone.replaceAll("\\D", "");

        if (digits.length() == 9) {
            digits = "998" + digits;
        }

        if (digits.length() != 12) {
            throw new AppBadException("Invalid phone number");
        }

        return digits;
    }


}
