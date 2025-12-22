package dasturlash.uz.service.article;

import dasturlash.uz.dto.KeyValueDTO;
import dasturlash.uz.repository.article.ArticleSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ArticleSectionService {

    @Autowired
    private ArticleSectionRepository articleSectionRepository;

//    public void create(String id, List<KeyValueDTO> sectionList) {
//        sectionList.forEach(section -> articleSectionRepository.insertSection(id, section.getId()));
//    }

    public void merge(String articleId, List<KeyValueDTO> dto){
        List<Integer> oldList = articleSectionRepository.getAllSectionIdByArticleId(articleId);
        List<Integer> newList = new LinkedList<>();
        dto.forEach(d -> newList.add(d.getId()));

        newList.stream().filter(id -> !oldList.contains(id)).forEach(id -> articleSectionRepository.insertSection(articleId, id));
        oldList.stream().filter(id -> !newList.contains(id)).forEach(id -> articleSectionRepository.deleteAllByArticleIdAndSectionId(articleId, id));
    }

//    public boolean deleteByArticleId(String id) {
//        return articleSectionRepository.deleteAllByArticleId(id) == 1;
//    }

    public List<KeyValueDTO> getAllSectionByArticleId(String articleId, String lang){
        return articleSectionRepository.getAllSectionByArticleId(articleId, lang);
    }
}
