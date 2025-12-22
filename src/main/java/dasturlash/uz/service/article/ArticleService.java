package dasturlash.uz.service.article;

import dasturlash.uz.dto.ImageDTO;
import dasturlash.uz.dto.KeyValueDTO;
import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.dto.RegionDTO;
import dasturlash.uz.dto.article.ArticleDTO;
import dasturlash.uz.dto.article.ArticleFullInfo;
import dasturlash.uz.dto.article.ArticleInfoDTO;
import dasturlash.uz.dto.article.ArticleShortInfo;
import dasturlash.uz.dto.filter.AdminFilterDTO;
import dasturlash.uz.dto.filter.FilterDTO;
import dasturlash.uz.dto.filter.FilterResultDTO;
import dasturlash.uz.entity.article.ArticleEntity;
import dasturlash.uz.enums.ArticleStatus;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.article.ArticleCustomRepository;
import dasturlash.uz.repository.article.ArticleRepository;
import dasturlash.uz.service.AttachService;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    @Autowired
    private ArticleCustomRepository articleCustomRepository;

    public ArticleInfoDTO create(ArticleDTO dto) {
        ArticleEntity article = new ArticleEntity();

        article.setTitle(dto.getTitle());
        article.setDescription(dto.getDescription());
        article.setContent(dto.getContent());
        article.setImageId(dto.getImage().getId());
        article.setRegionId(dto.getRegion().getId());
        article.setModeratorId(SpringSecurityUtil.currentProfileId());
        article.setStatus(ArticleStatus.NOT_PUBLISHED);

        article = articleRepository.save(article);

        articleCategoryService.merge(article.getId(), dto.getCategoryList());

        articleSectionService.merge(article.getId(), dto.getSectionList());


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
        article.setRegionId(dto.getRegion().getId());

        attachService.delete(article.getImageId());

        article.setImageId(dto.getImage().getId());

        articleCategoryService.merge(article.getId(), dto.getCategoryList());

        articleSectionService.merge(article.getId(), dto.getSectionList());

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

    public PageImpl<ArticleDTO> getLastNArticleBySectionId(Integer sectionId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ArticleShortInfo> lastNArticleBySectionId = articleRepository.getLastNArticleBySectionId(sectionId, pageRequest);
        List<ArticleDTO> resultList = new LinkedList<>();
        lastNArticleBySectionId.forEach(entity -> resultList.add(toDTO(entity)));
        return new PageImpl<>(resultList, pageRequest, lastNArticleBySectionId.getTotalElements());
    }

    public PageImpl<ArticleDTO> getLast12PublishedArticleExceptGivenIds(List<Integer> ids) {
        PageRequest pageRequest = PageRequest.of(1, 12);
        Page<ArticleShortInfo> last12PublishedArticleExceptGivenIds = articleRepository.getLast12PublishedArticleExceptGivenIds(ids, pageRequest);
        List<ArticleDTO> resultList = new LinkedList<>();
        last12PublishedArticleExceptGivenIds.forEach(entity -> resultList.add(toDTO(entity)));
        return new PageImpl<>(resultList, pageRequest, last12PublishedArticleExceptGivenIds.getTotalElements());
    }

    public PageImpl<ArticleDTO> getLastNArticleByCategoryId(Integer categoryId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ArticleShortInfo> lastNArticleByCategoryId = articleRepository.getLastNArticleByCategoryId(categoryId, pageRequest);
        List<ArticleDTO> resultList = new LinkedList<>();
        lastNArticleByCategoryId.forEach(entity -> resultList.add(toDTO(entity)));
        return new PageImpl<>(resultList, pageRequest, lastNArticleByCategoryId.getTotalElements());
    }

    public PageImpl<ArticleDTO> getLastNArticleByRegionId(Integer regionId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ArticleFullInfo> lastNArticleByRegionId = articleRepository.getLastNArticleByRegionId(regionId, pageRequest);
        List<ArticleDTO> resultList = new LinkedList<>();
        lastNArticleByRegionId.forEach(entity -> resultList.add(toDTO(entity)));
        return new PageImpl<>(resultList, pageRequest, lastNArticleByRegionId.getTotalElements());
    }

    public ArticleDTO getArticleByIdAndLang(String id, String lang) {
        increaseViewCount(id);
        return toDTO(articleRepository
                .getArticleByIdAndLang(id, lang)
                .orElseThrow(() -> new AppBadException("Article not found"))
        );
    }

//    public PageImpl<ArticleDTO> getLastNArticleByTagName(String tagName, int page, int size) {
//        PageRequest pageRequest = PageRequest.of(page, size);
//        Page<ArticleShortInfo> lastNArticleByTagName = articleRepository.getLastNArticleByTagName("%" + tagName + "%", pageRequest);
//        List<ArticleShortInfo> resultList = new LinkedList<>();
//        lastNArticleByTagName.forEach(entity -> resultList.add(toShortInfo(entity)));
//        return new PageImpl<>(resultList, pageRequest, lastNArticleByTagName.getTotalElements());
//    }

    public List<ArticleDTO> getLast4ArticleBySectionIdExceptOneArticleId(String articleId, Integer sectionId) {
        List<ArticleShortInfo> last4ArticleBySectionIdExceptOneArticleId = articleRepository.getLast4ArticleBySectionIdExceptOneArticleId(articleId, sectionId);
        List<ArticleDTO> resultList = new LinkedList<>();
        last4ArticleBySectionIdExceptOneArticleId.forEach(entity -> resultList.add(toDTO(entity)));
        return resultList;
    }

    public List<ArticleDTO> getLast4MostViewedArticleExceptOneArticleId(String articleId) {
        List<ArticleShortInfo> last4MostViewedArticleExceptOneArticleId = articleRepository.getLast4MostViewedArticleExceptOneArticleId(articleId);
        List<ArticleDTO> resultList = new LinkedList<>();
        last4MostViewedArticleExceptOneArticleId.forEach(entity -> resultList.add(toDTO(entity)));
        return resultList;
    }

    public Long increaseViewCount(String articleId) {
        articleRepository.increaseViewCountByArticleId(articleId);
        return articleRepository.getArticleViewCountByArticleId(articleId);
    }

    public Long increaseShareCount(String articleId) {
        articleRepository.increaseShareCountByArticleId(articleId);
        return articleRepository.getArticleShareCountByArticleId(articleId);
    }

    public PageImpl<ArticleDTO> filter(FilterDTO dto, int page, int size, boolean isModerator) {
        PageRequest pageRequest = PageRequest.of(page, size);
        FilterResultDTO<Object[]> filterResult = articleCustomRepository.filter(dto, page, size, isModerator);
        List<ArticleDTO> resultList = new LinkedList<>();
        filterResult.getContent().forEach(info -> {
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId((String) info[0]);
            articleDTO.setDescription((String) info[1]);
            articleDTO.setImage(new ImageDTO((String) info[2], null/*(String) info[3]*/));
            articleDTO.setPublishedDate((LocalDateTime) info[3]);
            articleDTO.setTitle((String) info[4]);
            resultList.add(articleDTO);
        });
        return new PageImpl<>(resultList, pageRequest, filterResult.getTotalCount());
    }

    public PageImpl<ArticleDTO> adminFilter(AdminFilterDTO dto, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        FilterResultDTO<Object[]> filterResult = articleCustomRepository.adminFilter(dto, page, size);
        List<ArticleDTO> resultList = new LinkedList<>();
        filterResult.getContent().forEach(info -> {
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId((String) info[0]);
            articleDTO.setDescription((String) info[1]);
            articleDTO.setImage(new ImageDTO((String) info[2], null/*(String) info[3]*/));
            articleDTO.setPublishedDate((LocalDateTime) info[3]);
            articleDTO.setTitle((String) info[4]);
            resultList.add(articleDTO);
        });
        return new PageImpl<>(resultList, pageRequest, filterResult.getTotalCount());
    }


    // get last n article
//    public List<ArticleDTO> getLastNArticle(Integer limit) {
//        List<ArticleShortInfo> resultList = articleRepository.getLastNArticle(limit); // 1
//        List<ArticleDTO> responseList = new LinkedList<>();
//        //  N + 1
//        resultList.forEach(mapper -> {
//            ArticleDTO articleDTO = toDTO(mapper);
//
//            ProfileDTO moderator = new ProfileDTO();
//            moderator.setId(mapper.getModeratorId());
//            moderator.setName(mapper.getModerName());
//            moderator.setUsername(mapper.getModerSurname());
//            articleDTO.setModerator(moderator);
//
//            responseList.add(articleDTO);
//        });
//
//        return responseList;
//    }

    private ArticleDTO toDTO(ArticleFullInfo fullINfo) {
        ArticleDTO dto = new ArticleDTO();
        ProfileDTO moderator = new ProfileDTO();
        moderator.setId(fullINfo.getModeratorId());
        moderator.setName(fullINfo.getModeratorName());
        dto.setId(fullINfo.getId());
        dto.setContent(fullINfo.getContent());
        dto.setTitle(fullINfo.getTitle());
        dto.setDescription(fullINfo.getDescription());
        dto.setModerator(moderator);
        String[] categoryId = fullINfo.getCategoryListId().split(",");
        String[] categoryName = fullINfo.getCategoryListName().split(",");

        List<KeyValueDTO> categoryList = new ArrayList<>();
        for (int i = 0; i < categoryId.length; i++) {
            KeyValueDTO keyValueDTO = new KeyValueDTO(Integer.parseInt(categoryId[i]), categoryName[i]);
            categoryList.add(keyValueDTO);
        }

        dto.setCategoryList(categoryList);

        String[] sectionId = fullINfo.getSectionListId().split(",");
        String[] sectionName = fullINfo.getSectionListName().split(",");

        List<KeyValueDTO> sectionList = new ArrayList<>();
        for (int i = 0; i < categoryId.length; i++) {
            KeyValueDTO keyValueDTO = new KeyValueDTO(Integer.parseInt(sectionId[i]), sectionName[i]);
            sectionList.add(keyValueDTO);
        }

        dto.setSectionList(sectionList);

        dto.setReadTime(fullINfo.getReadTime());
        dto.setPublishedDate(fullINfo.getPublishedDate());
        dto.setRegion(new RegionDTO(fullINfo.getRegionId(), fullINfo.getRegionName()));
        dto.setSharedCount(fullINfo.getSharedCount());
        dto.setViewCount(fullINfo.getViewCount());
        return dto;
    }

    private ArticleDTO toDTO(ArticleShortInfo shortInfo) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(shortInfo.getId());
        dto.setTitle(shortInfo.getTitle());
        dto.setDescription(shortInfo.getDescription());
        dto.setImage(new ImageDTO(shortInfo.getImageId(), shortInfo.getImageUrl()));
        dto.setPublishedDate(LocalDateTime.parse(shortInfo.getPublishedDate()));
        return dto;
    }

    private ArticleInfoDTO toInfo(ArticleEntity article) {
        ArticleInfoDTO articleInfoDTO = new ArticleInfoDTO();
        articleInfoDTO.setTitle(article.getTitle());
        articleInfoDTO.setDescription(article.getDescription());
        articleInfoDTO.setContent(article.getContent());
        articleInfoDTO.setImageId(articleInfoDTO.getImageId());
        articleInfoDTO.setRegionId(article.getRegionId());
        return articleInfoDTO;
    }
}
