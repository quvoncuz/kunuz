package dasturlash.uz.service;

import dasturlash.uz.dto.article.ArticleDTO;
import dasturlash.uz.dto.article.ArticleFullInfo;
import dasturlash.uz.dto.article.ArticleInfoDTO;
import dasturlash.uz.dto.article.ArticleShortInfo;
import dasturlash.uz.entity.ArticleEntity;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.ArticleRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleCategoryService articleCategoryService;

    @Autowired
    private ArticleSectionService articleSectionService;

    @Autowired
    private AttachService attachService;

    public ArticleInfoDTO create(ArticleDTO dto) {
        ArticleEntity article = new ArticleEntity();

        article.setTitle(dto.getTitle());
        article.setDescription(dto.getDescription());
        article.setContent(dto.getContent());
        article.setImageId(dto.getImageId());
        article.setRegionId(dto.getRegionId());
        article.setModeratorId(SpringSecurityUtil.currentProfileId());
        article.setStatus(ArticleStatus.NOT_PUBLISHED);

        article = articleRepository.save(article);

        articleCategoryService.create(article.getId(), dto.getCategoryList());

        articleSectionService.create(article.getId(), dto.getSectionList());


        ArticleInfoDTO info = toInfo(article);
        info.setCategoryList(dto.getCategoryList());
        info.setSectionList(dto.getSectionList());

        return info;
    }

    public ArticleInfoDTO update(String articleId, ArticleDTO dto) {
        ArticleEntity article = articleRepository
                .findById(articleId)
                .orElseThrow(() -> new AppBadException("Article not found"));
        article.setTitle(dto.getTitle());
        article.setDescription(dto.getDescription());
        article.setContent(dto.getContent());
        article.setRegionId(dto.getRegionId());

        attachService.delete(article.getImageId());

        article.setImageId(dto.getImageId());

        articleCategoryService.deleteByArticleId(article.getId());
        articleCategoryService.create(article.getId(), dto.getCategoryList());

        articleSectionService.deleteByArticleId(article.getId());
        articleSectionService.create(article.getId(), dto.getSectionList());

        article = articleRepository.save(article);

        ArticleInfoDTO info = toInfo(article);
        info.setCategoryList(dto.getCategoryList());
        info.setSectionList(dto.getSectionList());

        return info;
    }

    public String delete(String id) {
        if (articleRepository.changeVisibleFalse(id) == 1) {
            return "Successfully deleted";
        }
        return "Not found";
    }

    public String changeStatus(String id, ArticleStatus status) {
        if (articleRepository.changeStatus(id, status) == 1) {
            articleRepository.setPublisherId(id, SpringSecurityUtil.currentProfileId());
            articleRepository.setPublishedDate(id, LocalDateTime.now());
            return "Successfully changed";
        }
        return "Article not found";
    }

    public PageImpl<ArticleShortInfo> getLastNArticleBySectionId(Integer sectionId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ArticleEntity> lastNArticleBySectionId = articleRepository.getLastNArticleBySectionId(sectionId, pageRequest);
        List<ArticleShortInfo> resultList = new LinkedList<>();
        lastNArticleBySectionId.forEach(entity -> {
            ArticleShortInfo shortInfo = toShortInfo(entity);
            shortInfo.setCategoryList(articleCategoryService.getAllCategoryByArticleId(entity.getId(), "UZ"));
            resultList.add(shortInfo);
        });
        return new PageImpl<>(resultList, pageRequest, lastNArticleBySectionId.getTotalElements());
    }

    public PageImpl<ArticleShortInfo> getLast12PublishedArticleExceptGivenIds(List<Integer> ids) {
        PageRequest pageRequest = PageRequest.of(1, 12);
        Page<ArticleEntity> last12PublishedArticleExceptGivenIds = articleRepository.getLast12PublishedArticleExceptGivenIds(ids, pageRequest);
        List<ArticleShortInfo> resultList = new LinkedList<>();
        last12PublishedArticleExceptGivenIds.forEach(entity -> {
            ArticleShortInfo shortInfo = toShortInfo(entity);
            shortInfo.setCategoryList(articleCategoryService.getAllCategoryByArticleId(entity.getId(), "UZ"));
            resultList.add(shortInfo);
        });
        return new PageImpl<>(resultList, pageRequest, last12PublishedArticleExceptGivenIds.getTotalElements());
    }

    public PageImpl<ArticleShortInfo> getLastNArticleByCategoryId(Integer categoryId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ArticleEntity> lastNArticleByCategoryId = articleRepository.getLastNArticleByCategoryId(categoryId, pageRequest);
        List<ArticleShortInfo> resultList = new LinkedList<>();
        lastNArticleByCategoryId.forEach(entity -> {
            ArticleShortInfo shortInfo = toShortInfo(entity);
            shortInfo.setCategoryList(articleCategoryService.getAllCategoryByArticleId(entity.getId(), "UZ"));
            resultList.add(shortInfo);
        });
        return new PageImpl<>(resultList, pageRequest, lastNArticleByCategoryId.getTotalElements());
    }

    public PageImpl<ArticleFullInfo> getLastNArticleByRegionId(Integer regionId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ArticleEntity> lastNArticleByRegionId = articleRepository.getLastNArticleByRegionId(regionId, pageRequest);
        List<ArticleFullInfo> resultList = new LinkedList<>();
        lastNArticleByRegionId.forEach(entity -> {
            ArticleFullInfo article = toFullInfo(entity);
            article.setCategoryList(articleCategoryService.getAllCategoryByArticleId(entity.getId(), "UZ"));
            article.setSectionList(articleSectionService.getAllSectionByArticleId(entity.getId(), "UZ"));
            resultList.add(article);
        });
        return new PageImpl<>(resultList, pageRequest, lastNArticleByRegionId.getTotalElements());
    }

    public ArticleFullInfo getArticleByIdAndLang(String id, String lang) {
        ArticleEntity entity = articleRepository.getArticleById(id).orElseThrow(() -> new AppBadException("Article not found"));
        increaseViewCount(id);
        ArticleFullInfo article = toFullInfo(entity);
        article.setSectionList(articleSectionService.getAllSectionByArticleId(entity.getId(), lang.toUpperCase()));
        article.setCategoryList(articleCategoryService.getAllCategoryByArticleId(entity.getId(), lang.toUpperCase()));
        return article;
    }

    public PageImpl<ArticleShortInfo> getLastNArticleByTagName(String tagName, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ArticleEntity> lastNArticleByTagName = articleRepository.getLastNArticleByTagName("%" + tagName + "%", pageRequest);
        List<ArticleShortInfo> resultList = new LinkedList<>();
        lastNArticleByTagName.forEach(entity -> {
            ArticleShortInfo shortInfo = toShortInfo(entity);
            shortInfo.setCategoryList(articleCategoryService.getAllCategoryByArticleId(entity.getId(), "UZ"));
            resultList.add(shortInfo);
        });
        return new PageImpl<>(resultList, pageRequest, lastNArticleByTagName.getTotalElements());
    }

    public List<ArticleShortInfo> getLast4ArticleBySectionIdExceptOneArticleId(String articleId, Integer sectionId) {
        List<ArticleEntity> last4ArticleBySectionIdExceptOneArticleId = articleRepository.getLast4ArticleBySectionIdExceptOneArticleId(articleId, sectionId);
        List<ArticleShortInfo> resultList = new LinkedList<>();
        last4ArticleBySectionIdExceptOneArticleId.forEach(entity -> {
            ArticleShortInfo shortInfo = toShortInfo(entity);
            shortInfo.setCategoryList(articleCategoryService.getAllCategoryByArticleId(entity.getId(), "UZ"));
            resultList.add(shortInfo);
        });
        return resultList;
    }

    public List<ArticleShortInfo> getLast4MostViewedArticleExceptOneArticleId(String articleId) {
        List<ArticleEntity> last4MostViewedArticleExceptOneArticleId = articleRepository.getLast4MostViewedArticleExceptOneArticleId(articleId);
        List<ArticleShortInfo> resultList = new LinkedList<>();
        last4MostViewedArticleExceptOneArticleId.forEach(entity -> {
            ArticleShortInfo shortInfo = toShortInfo(entity);
            shortInfo.setCategoryList(articleCategoryService.getAllCategoryByArticleId(entity.getId(), "UZ"));
            resultList.add(shortInfo);
        });
        return resultList;
    }

    public Long increaseViewCount(String articleId){
        articleRepository.increaseViewCountByArticleId(articleId);
        return articleRepository.getArticleViewCountByArticleId(articleId);
    }
    public Long increaseShareCount(String articleId){
        articleRepository.increaseShareCountByArticleId(articleId);
        return articleRepository.getArticleShareCountByArticleId(articleId);
    }

    private ArticleFullInfo toFullInfo(ArticleEntity entity) {
        ArticleFullInfo article = new ArticleFullInfo();
        article.setDescription(entity.getDescription());
        article.setTitle(entity.getTitle());
        article.setImageId(entity.getImageId());
        article.setPublishedDate(entity.getPublishedDate());
        article.setViewCount(entity.getViewCount());
        article.setSharedCount(entity.getSharedCount());
        article.setRegionId(entity.getRegionId());
        article.setReadTime(entity.getReadTime());
        article.setModeratorId(entity.getModeratorId());
        article.setContent(entity.getContent());
        return article;
    }

    private ArticleShortInfo toShortInfo(ArticleEntity entity) {
        ArticleShortInfo article = new ArticleShortInfo();
        article.setDescription(entity.getDescription());
        article.setTitle(entity.getTitle());
        article.setImageId(entity.getImageId());
        article.setPublishedDate(entity.getPublishedDate());
        return article;
    }

    private ArticleDTO toDTO(ArticleEntity entity) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setImageId(entity.getImageId());
        dto.setModeratorId(entity.getModeratorId());
        dto.setPublisherId(entity.getPublisherId());
        dto.setReadTime(entity.getReadTime());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setRegionId(entity.getRegionId());
        dto.setSharedCount(entity.getSharedCount());
        dto.setViewCount(entity.getViewCount());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    private ArticleInfoDTO toInfo(ArticleEntity article){
        ArticleInfoDTO articleInfoDTO = new ArticleInfoDTO();
        articleInfoDTO.setTitle(article.getTitle());
        articleInfoDTO.setDescription(article.getDescription());
        articleInfoDTO.setContent(article.getContent());
        articleInfoDTO.setImageId(articleInfoDTO.getImageId());
        articleInfoDTO.setRegionId(article.getRegionId());
        return articleInfoDTO;
    }
}
