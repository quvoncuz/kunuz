package dasturlash.uz.service;

import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.dto.ProfileInfoDTO;
import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.repository.ProfileRoleRepository;
import dasturlash.uz.util.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    public ProfileInfoDTO login(String username, String password) {
        Optional<ProfileEntity> entity = profileRepository.findByUsername(username);
        if (entity.isEmpty()) {
            throw new AppBadException("Profile not found");
        }
        if (!entity.get().getPassword().equals(MD5Encoder.encoder(password))) {
            throw new AppBadException("Wrong Password");
        }

        return toInfoDTO(entity.get());
    }

    public ProfileDTO register(ProfileDTO profileDTO) {
        Optional<ProfileEntity> byUsername = profileRepository.findByUsername(profileDTO.getUsername());
        if (byUsername.isPresent()) {
            throw new AppBadException("Username already exists");
        }
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName(profileDTO.getName());
        profileEntity.setSurname(profileDTO.getSurname());
        profileEntity.setStatus(Status.ACTIVE);
        profileEntity.setUsername(profileDTO.getUsername());
        profileEntity.setPassword(MD5Encoder.encoder(profileDTO.getPassword()));
        profileEntity = profileRepository.save(profileEntity);

        profileDTO.setId(profileEntity.getId());
        profileDTO.setCreatedDate(profileEntity.getCreatedDate());
        return profileDTO;
    }

    private ProfileInfoDTO toInfoDTO(ProfileEntity profileEntity) {
        ProfileInfoDTO profileInfoDTO = new ProfileInfoDTO();
        profileInfoDTO.setUsername(profileEntity.getUsername());
        profileInfoDTO.setSurname(profileEntity.getSurname());
        profileInfoDTO.setName(profileEntity.getName());
        profileInfoDTO.setRoles(profileRoleRepository.findAllByProfileId(profileEntity.getId()));
        profileInfoDTO.setPhotoId(profileEntity.getPhotoId());
        return profileInfoDTO;
    }

    public Boolean confirm(String sms){
        return true;
    }
}
