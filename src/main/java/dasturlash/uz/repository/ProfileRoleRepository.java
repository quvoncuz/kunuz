package dasturlash.uz.repository;

import dasturlash.uz.entity.ProfileRoleEntity;
import dasturlash.uz.enums.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRoleRepository extends JpaRepository<ProfileRoleEntity, Integer> {
    List<Role> findAllByProfileId(Integer id);

    @Modifying
    @Transactional
    void deleteByProfileId(Integer id);
}
