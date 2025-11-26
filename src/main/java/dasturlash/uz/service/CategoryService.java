package dasturlash.uz.service;

import dasturlash.uz.dto.CategoryDTO;
import dasturlash.uz.entity.CategoryEntity;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO create(CategoryDTO categoryDTO) {

        if (categoryRepository.findByKey(categoryDTO.getKey()).isPresent()) {
            throw new AppBadException("Key already exists");
        }
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setKey(categoryDTO.getKey());
        categoryEntity.setNameUz(categoryDTO.getNameUz());
        categoryEntity.setNameRu(categoryDTO.getNameRu());
        categoryEntity.setNameEn(categoryDTO.getNameEn());
        categoryEntity.setOrderNumber(categoryDTO.getOrderNumber());

        categoryRepository.save(categoryEntity);
        categoryDTO.setCreatedDate(categoryEntity.getCreatedDate());
        categoryDTO.setId(categoryEntity.getId());
        return categoryDTO;
    }

    public CategoryDTO update(Integer id, CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = categoryRepository.findById(id).orElseThrow(() -> new AppBadException("Category not found"));
        Optional<CategoryEntity> check = categoryRepository.findByKey(categoryDTO.getKey());
        if (check.isPresent() && !check.get().getId().equals(categoryEntity.getId())) {
            throw new AppBadException("Category not found");
        }
        categoryEntity.setKey(categoryDTO.getKey());
        categoryEntity.setNameUz(categoryDTO.getNameUz());
        categoryEntity.setNameRu(categoryDTO.getNameRu());
        categoryEntity.setNameEn(categoryDTO.getNameEn());
        categoryEntity.setOrderNumber(categoryDTO.getOrderNumber());

        CategoryEntity entity = categoryRepository.save(categoryEntity);

        categoryDTO.setCreatedDate(entity.getCreatedDate());
        categoryDTO.setId(entity.getId());
        return categoryDTO;
    }

    public Boolean delete(Integer id) {
        return categoryRepository.changeVisibleFalseById(id) == 1;
    }

    public List<CategoryEntity> getAll() {
        return categoryRepository.findAllByVisibleTrueOrderByOrderNumberAsc();
    }

    public List<CategoryDTO> getAllByLang(String lang){
        List<CategoryEntity> categoryEntities = categoryRepository.findAllByVisibleTrueOrderByOrderNumberAsc();
        List<CategoryDTO> resultList = new LinkedList<>();
        categoryEntities.forEach(entity -> {
            resultList.add(toDTOByLang(entity, lang.toUpperCase()));
        });
        return resultList;
    }

    private CategoryDTO toDTOByLang(CategoryEntity categoryEntity, String lang) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setOrderNumber(categoryEntity.getOrderNumber());
        categoryDTO.setKey(categoryEntity.getKey());
        categoryDTO.setId(categoryEntity.getId());
        categoryDTO.setCreatedDate(categoryEntity.getCreatedDate());
        switch (lang){
            case "EN"-> categoryDTO.setName(categoryEntity.getNameEn());
            case "RU"-> categoryDTO.setName(categoryEntity.getNameRu());
            case "UZ"-> categoryDTO.setName(categoryEntity.getNameUz());
        }
        return categoryDTO;
    }

    private CategoryDTO toDTO(CategoryEntity categoryEntity) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categoryEntity.getId());
        categoryDTO.setKey(categoryEntity.getKey());
        categoryDTO.setNameUz(categoryEntity.getNameUz());
        categoryDTO.setNameRu(categoryEntity.getNameRu());
        categoryDTO.setNameEn(categoryEntity.getNameEn());
        categoryDTO.setOrderNumber(categoryEntity.getOrderNumber());
        categoryDTO.setCreatedDate(categoryEntity.getCreatedDate());
        return categoryDTO;
    }
}
