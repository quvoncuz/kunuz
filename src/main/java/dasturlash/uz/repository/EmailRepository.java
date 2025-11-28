package dasturlash.uz.repository;

import dasturlash.uz.entity.EmailEntity;
import dasturlash.uz.enums.SmsStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Integer> {

    @Query(value = "select * from email where email = ?1 order by created_date desc limit 1", nativeQuery = true)
    Optional<EmailEntity> findByUsername(@Valid String email);

    @Modifying
    @Transactional
    @Query("update EmailEntity set smsStatus = ?2 where id = ?1")
    void updateStatus(Integer id, SmsStatus smsStatus);
}
