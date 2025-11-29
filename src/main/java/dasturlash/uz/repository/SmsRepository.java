package dasturlash.uz.repository;

import dasturlash.uz.entity.SmsEntity;
import dasturlash.uz.enums.SmsStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SmsRepository extends JpaRepository<SmsEntity, Integer> {
    @Query(value = "select * from sms where phone = ?1 order by created_date desc limit 1", nativeQuery = true)
    Optional<SmsEntity> findByUsername(@Valid String phone);

    @Modifying
    @Transactional
    @Query("update SmsEntity set smsStatus = ?2 where id = ?1")
    void updateStatus(Integer id, SmsStatus smsStatus);

    List<SmsEntity> findAllByPhone(String phone);
}
