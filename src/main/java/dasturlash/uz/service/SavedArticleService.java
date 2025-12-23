package dasturlash.uz.service;

import dasturlash.uz.dto.ImageDTO;
import dasturlash.uz.dto.article.ArticleDTO;
import dasturlash.uz.dto.saved.SavedArticleDTO;
import dasturlash.uz.dto.saved.SavedArticleMapper;
import dasturlash.uz.entity.SavedArticleEntity;
import dasturlash.uz.repository.SavedArticleRepository;
import dasturlash.uz.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SavedArticleService {
    @Autowired
    private SavedArticleRepository savedArticleRepository;

    public String create(String articleId, String token){
        Optional<SavedArticleEntity> byArticleId = savedArticleRepository.findByArticleId(articleId);
        if (byArticleId.isPresent()){
            SavedArticleEntity savedArticle = byArticleId.get();
            savedArticleRepository.delete(savedArticle);
            return "Article removed from saved!";
        } else {
            SavedArticleEntity savedArticle = new SavedArticleEntity();
            savedArticle.setArticleId(articleId);
            Integer profileId = JwtUtil.decode(token).getId();
            savedArticle.setProfileId(profileId);
            return "Article added to saved!";
        }
    }

    public List<SavedArticleDTO> getAllByProfileId(String token){
        Integer profileId = JwtUtil.decode(token).getId();
        List<SavedArticleMapper> articles = savedArticleRepository.findSavedArticleEntitiesByProfileId(profileId);
        List<SavedArticleDTO> resultList = new LinkedList<>();
        articles.forEach(article -> resultList.add(toDTO(article)));
        return resultList;
    }

    private SavedArticleDTO toDTO(SavedArticleMapper articles){
        SavedArticleDTO dto = new SavedArticleDTO();
        ImageDTO imageDTO = new ImageDTO(articles.getArticleImageId(), articles.getArticleImageUrl());
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setImage(imageDTO);
        articleDTO.setId(articles.getArticleId());
        articleDTO.setTitle(articles.getArticleTitle());
        articleDTO.setDescription(articles.getArticleDescription());
        dto.setId(articles.getId());
        dto.setArticle(articleDTO);
        dto.setSavedTime(articles.getSavedDate());
        return dto;
    }
}
