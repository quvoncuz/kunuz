package dasturlash.uz.service.article;

import dasturlash.uz.dto.KeyValueDTO;
import dasturlash.uz.repository.article.ArticleCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleCategoryService {

    @Autowired
    private ArticleCategoryRepository articleCategoryRepository;

    public void create(String id, List<KeyValueDTO> categoryList) {
        categoryList.forEach(category-> articleCategoryRepository.insertCategory(id, category.getId()));
    }

    public void merge(String articleId, List<KeyValueDTO> dto){
        List<Integer> oldList = articleCategoryRepository.getAllCategoryIdByArticleId(articleId);
        List<Integer> newList = new LinkedList<>();
        dto.forEach(d -> newList.add(d.getId()));

        newList.stream().filter(id -> !oldList.contains(id)).forEach(id -> articleCategoryRepository.insertCategory(articleId, id));
        oldList.stream().filter(id -> !newList.contains(id)).forEach(id -> articleCategoryRepository.deleteAllByArticleIdAndCategoryId(articleId, id));
    }

    public boolean deleteByArticleId(String id) {
        return articleCategoryRepository.deleteAllByArticleId(id) == 1;
    }

    public List<KeyValueDTO> getAllCategoryByArticleId(String articleId, String lang) {
        return articleCategoryRepository.getAllCategoryByArticleId(articleId, lang);
    }
}
