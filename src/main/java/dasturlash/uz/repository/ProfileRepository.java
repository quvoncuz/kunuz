package dasturlash.uz.repository;

import dasturlash.uz.entity.ProfileEntity;
import dasturlash.uz.enums.Status;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer>, PagingAndSortingRepository<ProfileEntity, Integer> {
    Optional<ProfileEntity> findByUsername(@NotBlank String username);

    Page<ProfileEntity> findAllByVisibleIsTrue(Pageable pageable);

    @Modifying
    @Transactional
    @Query("update ProfileEntity set visible = false where id = ?1")
    int updateVisibleById(Integer id);

    Optional<ProfileEntity> findByUsernameAndVisibleIsTrue(@NotBlank(message = "Username required") String username);
}
