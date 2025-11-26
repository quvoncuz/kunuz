package dasturlash.uz.service;

import dasturlash.uz.dto.SectionDTO;
import dasturlash.uz.entity.SectionEntity;
import dasturlash.uz.exps.AppBadException;
import dasturlash.uz.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    public SectionDTO create(SectionDTO sectionDTO) {
        Optional<SectionEntity> section = sectionRepository.findByKey(sectionDTO.getKey());
        if (section.isPresent()) {
            throw new AppBadException("Section key already exists");
        }
        SectionEntity sectionEntity = new SectionEntity();
        sectionEntity.setKey(sectionDTO.getKey());
        sectionEntity.setImageId(sectionDTO.getImageId());
        sectionEntity.setNameUz(sectionDTO.getNameUz());
        sectionEntity.setNameRu(sectionDTO.getNameRu());
        sectionEntity.setNameEn(sectionDTO.getNameEn());
        sectionEntity.setOrderNumber(sectionDTO.getOrderNumber());
        SectionEntity entity = sectionRepository.save(sectionEntity);

        sectionDTO.setId(entity.getId());
        sectionDTO.setCreatedDate(entity.getCreatedDate());

        return sectionDTO;
    }

    public SectionDTO update(Integer id, SectionDTO sectionDTO) {
        SectionEntity sectionEntity = sectionRepository
                .findById(id)
                .orElseThrow(() -> new AppBadException("Section not found"));

        Optional<SectionEntity> check = sectionRepository.findByKey(sectionDTO.getKey());
        if (check.isPresent() && !check.get().getId().equals(id)) {
            throw new AppBadException("Section key already exists");
        }

        sectionEntity.setNameUz(sectionDTO.getNameUz());
        sectionEntity.setNameRu(sectionDTO.getNameRu());
        sectionEntity.setNameEn(sectionDTO.getNameEn());
        sectionEntity.setOrderNumber(sectionDTO.getOrderNumber());
        sectionEntity.setKey(sectionDTO.getKey());
        sectionRepository.save(sectionEntity);
        sectionDTO.setId(sectionEntity.getId());
        sectionDTO.setCreatedDate(sectionEntity.getCreatedDate());
        return sectionDTO;
    }

    public PageImpl<SectionEntity> pagination(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<SectionEntity> sectionEntities = sectionRepository.findAll(pageRequest);

        List<SectionEntity> resultList = new LinkedList<>();
        sectionEntities.forEach(resultList::add);

        return new PageImpl<>(resultList, pageRequest, sectionEntities.getTotalElements());
    }

    public Boolean deleteById(Integer id) {
        return sectionRepository.changeVisibleToFalse(id) == 1;
    }

    public PageImpl<SectionDTO> getAllByLang(String lang, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<SectionEntity> sectionEntities = sectionRepository.findAllByVisibleIsTrue(pageRequest);
        List<SectionDTO> resultList = new LinkedList<>();
        sectionEntities.forEach(sectionEntity -> {
            resultList.add(toDTOByLang(sectionEntity, lang));
        });

        return new PageImpl<>(resultList, pageRequest, sectionEntities.getTotalElements());
    }

    private SectionDTO toDTOByLang(SectionEntity sectionEntity, String lang) {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setKey(sectionEntity.getKey());
        sectionDTO.setId(sectionEntity.getId());
        switch (lang){
            case "UZ"->  sectionDTO.setName(sectionEntity.getNameUz());
            case "EN"->  sectionDTO.setName(sectionEntity.getNameEn());
            case "RU"->  sectionDTO.setName(sectionEntity.getNameRu());
        }
        return sectionDTO;
    }

    private SectionDTO toDTO(SectionEntity sectionEntity) {
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setId(sectionEntity.getId());
        sectionDTO.setNameUz(sectionEntity.getNameUz());
        sectionDTO.setNameRu(sectionEntity.getNameRu());
        sectionDTO.setNameEn(sectionEntity.getNameEn());
        sectionDTO.setOrderNumber(sectionEntity.getOrderNumber());
        sectionDTO.setKey(sectionEntity.getKey());
        sectionDTO.setCreatedDate(sectionEntity.getCreatedDate());
        sectionDTO.setImageId(sectionEntity.getImageId());
        return sectionDTO;
    }
}
