package dasturlash.uz.service;

import dasturlash.uz.dto.AttachDTO;
import dasturlash.uz.entity.AttachEntity;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class AttachService {

    @Autowired
    private AttachRepository attachRepository;

    @Value("${attach.dir}")
    private String folderName;

    public AttachDTO upload(MultipartFile file) {
        String pathFolder = getYmDString();
        String key = UUID.randomUUID().toString();
        String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            File folder = new File(folderName + "/" + pathFolder);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderName + "/" + pathFolder + "/" + key + extension);
            Files.write(path, bytes);

            AttachEntity attachEntity = new AttachEntity();
            attachEntity.setId(key + extension);
            attachEntity.setSize(file.getSize());
            attachEntity.setPath(pathFolder);
            attachEntity.setOriginName(file.getOriginalFilename());
            attachEntity.setExtension(extension);
            attachRepository.save(attachEntity);
            return toDTO(attachEntity);
        } catch (Exception e) {
            throw new AppBadException(e.getMessage());
        }
    }

    public ResponseEntity<Resource> open(String id) {
        AttachEntity attachEntity = getAttach(id);
        System.out.println(attachEntity);
        Path filePath = Paths.get(getPath(attachEntity)).normalize();
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new AppBadException("File not found");
            }
            String contentType = Files.probeContentType(filePath);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<Resource> download(String id) {
        try {
            AttachEntity entity = getAttach(id);
            Path filePath = Paths.get(getPath(entity)).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + entity.getOriginName() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not read the file!");
        }
    }

    public boolean delete(String id) {
        AttachEntity entity = getAttach(id);
//        return attachRepository.changeVisiable(id) == 1;
        attachRepository.delete(entity);
        File file = new File(getPath(entity));
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public PageImpl<AttachDTO> pagination(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<AttachEntity> paging = attachRepository.findAll(pageRequest);

        List<AttachDTO> resultList = new LinkedList<>();

        paging.forEach(attachEntity -> {
            resultList.add(toDTO(attachEntity));
        });
        return new PageImpl<>(resultList, pageRequest, paging.getTotalElements());
    }


    private AttachEntity getAttach(String id) {
        return attachRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Attach not found"));
    }

    private String getYmDString() {
        String year = LocalDate.now().getYear() + "";
        String month = LocalDate.now().getMonthValue() + 1 + "";
        String day = LocalDate.now().getDayOfMonth() + "";
        return year + "/" + month + "/" + day;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String getPath(AttachEntity entity) {
        return folderName + "/" + entity.getPath() + "/" + entity.getId();
    }

    private AttachDTO toDTO(AttachEntity attachEnttity) {
        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(attachEnttity.getId());
        attachDTO.setSize(attachEnttity.getSize());
        attachDTO.setOriginName(attachEnttity.getOriginName());
        attachDTO.setExtension(attachEnttity.getExtension());
        attachDTO.setCreatedData(attachEnttity.getCreatedDate());
        attachDTO.setUrl(attachEnttity.getPath());
        return attachDTO;
    }
}
