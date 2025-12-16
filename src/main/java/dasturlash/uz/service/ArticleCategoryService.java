package dasturlash.uz.service;

import dasturlash.uz.dto.KeyValueDTO;
import dasturlash.uz.repository.ArticleCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleCategoryService {

    @Autowired
    private ArticleCategoryRepository articleCategoryRepository;

    public void create(String id, List<KeyValueDTO> categoryList) {
        categoryList.forEach(category-> articleCategoryRepository.insertCategory(id, category.getId()));
    }

    public boolean deleteByArticleId(String id) {
        return articleCategoryRepository.deleteAllByArticleId(id) == 1;
    }

    public List<KeyValueDTO> getAllCategoryByArticleId(String articleId, String lang) {
        return articleCategoryRepository.getAllCategoryByArticleId(articleId, lang);
    }
}
