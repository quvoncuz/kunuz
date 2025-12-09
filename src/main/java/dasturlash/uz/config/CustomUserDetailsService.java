package dasturlash.uz.config;

import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ProfileRepository profileRepository;

    private ProfileRoleRepository profileRoleRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ProfileEntity profile = profileRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        CustomUserDetails customUser = new CustomUserDetails();
        customUser.setId(profile.getId());
        customUser.setUsername(username);
        customUser.setPassword(profile.getPassword());
        customUser.setVisible(profile.getVisible());
        customUser.setRole(profileRoleRepository.getRoleListByProfileId(profile.getId()));
        return customUser;
    }
}
