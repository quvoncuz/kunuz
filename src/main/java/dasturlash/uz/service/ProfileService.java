package dasturlash.uz.service;

import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.dto.ProfileInfoDTO;
import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.entity.ProfileRoleEntity;
import dasturlash.uz.enums.Status;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    public ProfileInfoDTO create(ProfileDTO profileDTO) {
        Optional<ProfileEntity> profile = getProfileByUsername(profileDTO.getUsername());
        if (profile.isPresent()) {
            throw new AppBadException("Username not available");
        }
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName(profileDTO.getName());
        profileEntity.setSurname(profileDTO.getSurname());
        profileEntity.setUsername(profileDTO.getUsername());
        profileEntity.setPassword(profileDTO.getPassword());
        profileEntity.setStatus(Status.ACTIVE.toString());
        ProfileEntity entity = profileRepository.save(profileEntity);

        profileDTO.setId(entity.getId());
        profileDTO.setCreatedDate(entity.getCreatedDate());

        profileDTO.getRoles().forEach(role -> {
            profileRoleRepository.save(new ProfileRoleEntity(profileEntity.getId(), role));
        });

        return toInfoDTO(profileEntity);
    }

    public ProfileDTO getById(Integer id) {
        Optional<ProfileEntity> profile = getProfileById(id);
        if (profile.isPresent()) {
            return toDTO(profile.get());
        }
        throw new AppBadException("Profile not found");
    }

    public ProfileDTO updateByAdmin(Integer id, ProfileDTO profileDTO) {
        // 1. Profile borligini tekshirish
        ProfileEntity profileEntity = getProfileById(id)
                .orElseThrow(() -> new AppBadException("Profile not found"));

        // 2. Username boshqa profilega tegishli emasligini tekshirish
        getProfileByUsername(profileDTO.getUsername())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new AppBadException("Username not available");
                    }
                });

        // 3. Fieldlarni yangilash
        profileEntity.setName(profileDTO.getName());
        profileEntity.setSurname(profileDTO.getSurname());
        profileEntity.setUsername(profileDTO.getUsername());
        profileEntity.setPassword((profileDTO.getPassword()));
        profileEntity.setPhotoId(profileDTO.getPhotoId());

        // 4. Roleni yangilash
        if (profileDTO.getRoles() != null) {
            profileRoleRepository.deleteByProfileId(profileEntity.getId());

            profileDTO.getRoles().forEach(role ->
                    profileRoleRepository.save(new ProfileRoleEntity(profileEntity.getId(), role))
            );
        }

        profileRepository.save(profileEntity);

        // 6. Responsega tayyorlash
        profileDTO.setId(profileEntity.getId());
        profileDTO.setCreatedDate(profileEntity.getCreatedDate());

        return profileDTO;
    }

    public ProfileInfoDTO update(Integer id, ProfileInfoDTO profileDTO) {
        Optional<ProfileEntity> profile = profileRepository.findById(id);
        Optional<ProfileEntity> profileByUsername = getProfileByUsername(profileDTO.getUsername());
        if (profile.isEmpty()) {
            throw new AppBadException("Profile not found");
        }
        if (profileByUsername.isPresent() && !profileByUsername.get().getId().equals(id)) {
            throw new AppBadException("Username not available");
        }
        ProfileEntity profileEntity = profile.get();
        profileEntity.setName(profileDTO.getName());
        profileEntity.setSurname(profileDTO.getSurname());
        profileEntity.setUsername(profileDTO.getUsername());
        profileEntity.setPhotoId(profileDTO.getPhotoId());
        profileRepository.save(profileEntity);
        return toInfoDTO(profileEntity);
    }

    public PageImpl<ProfileDTO> pagination(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProfileEntity> profileEntities = profileRepository.findAllByVisibleIsTrue(pageRequest);

        long totalCount = profileEntities.getTotalElements();

        List<ProfileDTO> resultList = new LinkedList<>();
        profileEntities.forEach(profileEntity -> {
            resultList.add(toDTO(profileEntity));
        });
        return new PageImpl<>(resultList, pageRequest, totalCount);
    }

    public Boolean deleteById(Integer id) {
        return profileRepository.updateVisibleById(id) == 1;
    }

    public Boolean updatePassword(Integer id, String oldPassword, String newPassword) {
        Optional<ProfileEntity> profile = profileRepository.findById(id);
        if (profile.isEmpty()) {
            throw new AppBadException("Profile not found");
        }
        if (!profile.get().getPassword().equals(oldPassword)) {
            throw new AppBadException("Passwords do not match");
        }
        ProfileEntity profileEntity = profile.get();
        profileEntity.setPassword(newPassword);
        profileRepository.save(profileEntity);
        return true;
    }

    private Optional<ProfileEntity> getProfileById(Integer id) {
        return profileRepository.findById(id);
    }

    private Optional<ProfileEntity> getProfileByUsername(String username) {
        return profileRepository.findByUsername(username);
    }

    private ProfileInfoDTO toInfoDTO(ProfileEntity profileEntity) {
        ProfileInfoDTO profileInfoDTO = new ProfileInfoDTO();
        profileInfoDTO.setUsername(profileEntity.getUsername());
        profileInfoDTO.setSurname(profileEntity.getSurname());
        profileInfoDTO.setName(profileEntity.getName());
        profileInfoDTO.setPhotoId(profileEntity.getPhotoId());
        profileInfoDTO.setRoles(
                profileRoleRepository.findAllByProfileId(profileEntity.getId())
                        .stream()
                        .map(ProfileRoleEntity::getRole)
                        .toList());
        return profileInfoDTO;
    }

    private ProfileDTO toDTO(ProfileEntity profileEntity) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(profileEntity.getId());
        profileDTO.setUsername(profileEntity.getUsername());
        profileDTO.setSurname(profileEntity.getSurname());
        profileDTO.setName(profileEntity.getName());
        profileDTO.setPhotoId(profileEntity.getPhotoId());
        profileDTO.setCreatedDate(profileEntity.getCreatedDate());
        profileDTO.setRoles(
                profileRoleRepository.findAllByProfileId(profileEntity.getId())
                        .stream()
                        .map(ProfileRoleEntity::getRole)
                        .toList());
        return profileDTO;
    }

}
