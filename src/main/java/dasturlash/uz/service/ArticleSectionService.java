package dasturlash.uz.service;

import dasturlash.uz.dto.KeyValueDTO;
import dasturlash.uz.repository.ArticleSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleSectionService {

    @Autowired
    private ArticleSectionRepository articleSectionRepository;

    public void create(String id, List<KeyValueDTO> sectionList) {
        sectionList.forEach(section -> articleSectionRepository.insertSection(id, section.getId()));
    }

    public boolean deleteByArticleId(String id) {
        return articleSectionRepository.deleteAllByArticleId(id) == 1;
    }

    public List<KeyValueDTO> getAllSectionByArticleId(String articleId, String lang){
        return articleSectionRepository.getAllSectionByArticleId(articleId, lang);
    }
}
