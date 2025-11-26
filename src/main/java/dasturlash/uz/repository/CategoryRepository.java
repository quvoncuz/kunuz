package dasturlash.uz.repository;

import dasturlash.uz.entity.CategoryEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    Optional<CategoryEntity> findByKey(@NotBlank String key);

    @Modifying
    @Transactional
    @Query("update CategoryEntity set visible = false where id = ?1")
    int changeVisibleFalseById(Integer id);

    List<CategoryEntity> findAllByVisibleTrueOrderByOrderNumberAsc();
}
