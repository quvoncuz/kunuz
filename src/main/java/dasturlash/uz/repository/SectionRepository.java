package dasturlash.uz.repository;

import dasturlash.uz.entity.SectionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, Integer> {

    Optional<SectionEntity> findByKey(String key);

    List<SectionEntity> findByVisibleIsTrue();

    Page<SectionEntity> findAllByVisibleIsTrue(Pageable pageable);

    @Modifying
    @Transactional
    @Query("update SectionEntity set visible = false where id = ?1")
    int changeVisibleToFalse(Integer id);
}
