package dasturlash.uz.service.comment;

import dasturlash.uz.dto.ImageDTO;
import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.dto.comment.CommentDTO;
import dasturlash.uz.dto.comment.CommentShortInfo;
import dasturlash.uz.dto.filter.FilterCommentDTO;
import dasturlash.uz.dto.filter.FilterResultDTO;
import dasturlash.uz.entity.CommentEntity;
import dasturlash.uz.enums.Role;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.comment.CommentCustomRepository;
import dasturlash.uz.repository.comment.CommentRepository;
import dasturlash.uz.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentLikeService commentLikeService;
    
    @Autowired
    private CommentCustomRepository commentCustomRepository;

    public CommentDTO mergeComment(CommentDTO dto, String commentId){
        Optional<CommentEntity> byId = commentRepository.findById(commentId);
        if (byId.isPresent()){
            CommentEntity comment = byId.get();
            if (!comment.getProfileId().equals(dto.getProfile().getId())){
                throw new AppBadException("Profile mismatch!");
            }
            comment.setContent(dto.getContent());
            comment.setUpdatedDate(LocalDateTime.now());
            comment = commentRepository.save(comment);
            dto.setCreatedDate(comment.getUpdatedDate());
            return dto;
        } else {
            CommentEntity comment = new CommentEntity();
            comment.setContent(dto.getContent());
            comment.setArticleId(dto.getArticle().getId());
            comment.setProfileId(dto.getProfile().getId());
            comment.setReplyId(dto.getReplyId());
            comment = commentRepository.save(comment);
            dto.setId(comment.getId());
            dto.setCreatedDate((comment.getUpdatedDate() != null) ? comment.getUpdatedDate() : comment.getCreatedDate());
            return dto;
        }
    }
    public boolean delete(String commentId, String token){
        
        Integer profileId = JwtUtil.decode(token).getId();
        CommentEntity comment = commentRepository.findByIdAndVisibleIsTrue(commentId)
                .orElseThrow(() -> new AppBadException("Comment Not Found"));
        if (!Objects.equals(comment.getProfileId(), profileId) ||
                !JwtUtil.decode(token).getRole().contains(Role.ROLE_ADMIN.toString())){
            throw new AppBadException("You are not OWNER!");
        }
        comment.setVisible(false);
        commentRepository.save(comment);
        return true;
    }
    
    public PageImpl<CommentDTO> filter(FilterCommentDTO dto, int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        FilterResultDTO<Object[]> filter = commentCustomRepository.filter(dto, page, size);
        List<CommentDTO> resultList = new LinkedList<>();
        filter.getContent().forEach(objects -> {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId((String) objects[0]);
            commentDTO.setContent((String) objects[1]);
            commentDTO.setCreatedDate((LocalDateTime) objects[2]);
            commentDTO.setUpdatedDate((LocalDateTime) objects[3]);
            commentDTO.getProfile().setId((Integer) objects[4]);
            commentDTO.getProfile().setName((String) objects[5]);
            commentDTO.setReplyId((String) objects[6]);
            commentDTO.getArticle().setId((String) objects[7]);
            commentDTO.getArticle().setTitle((String) objects[8]);
            commentDTO.setVisible((Boolean) objects[9]);
            commentDTO.setLikeCount((Long) objects[10]);
            commentDTO.setDislikeCount((Long) objects[11]);
            resultList.add(commentDTO);
        });
        return new PageImpl<>(resultList, pageRequest, filter.getTotalCount());
    }

    public List<CommentDTO> getRepliedCommentListByCommentId(String commentId){
        List<CommentShortInfo> allByReplyId = commentRepository.findAllByReplyId(commentId);
        List<CommentDTO> resultList = new LinkedList<>();
        allByReplyId.forEach(info -> resultList.add(toDTO(info)));
        return resultList;
    }

    public List<CommentDTO> getCommentListByArticleId(String articleId){
        List<CommentShortInfo> allByArticleId = commentRepository.findAllByArticleId(articleId);
        List<CommentDTO> resultList = new LinkedList<>();
        allByArticleId.forEach(info -> resultList.add(toDTO(info)));
        return resultList;
    }

    private CommentDTO toDTO(CommentShortInfo info){

        CommentDTO dto = new CommentDTO();
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(info.getProfileId());
        profileDTO.setName(info.getProfileName());
        profileDTO.setSurname(info.getProfileSurname());
        profileDTO.setImage(new ImageDTO(info.getProfileImageId(), info.getProfileImageUrl()));
        dto.setId(info.getId());
        dto.setProfile(profileDTO);
        dto.setCreatedDate(info.getCreatedDate());
        dto.setUpdatedDate(info.getUpdatedDate());
        dto.setLikeCount(info.getLikeCount());
        dto.setDislikeCount(info.getDislikeCount());
        return dto;
    }
    
    private CommentDTO toDTO(CommentEntity comment){
        CommentDTO dto = new CommentDTO();
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(comment.getProfileId());
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setReplyId(comment.getReplyId());
        dto.setProfile(profileDTO);
        dto.getArticle().setId(comment.getArticleId());
        dto.setCreatedDate(comment.getCreatedDate());
        dto.setUpdatedDate(comment.getUpdatedDate());
        return dto;
    }

}
