package dasturlash.uz.repository;

import dasturlash.uz.entity.RegionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface RegionRepository extends CrudRepository<RegionEntity, Integer> {

    Optional<RegionEntity> findByRegionKey(String regionKey);

    Optional<RegionEntity> findByIdAndVisibleIsTrue(Integer id);

    @Modifying
    @Transactional
    @Query("update RegionEntity set visible = ?2 where id = ?1 ")
    int updateVisibleById(Integer id, Boolean visible);

    Iterable<RegionEntity> findAllByVisibleIsTrue();
}
