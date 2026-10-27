package com.rainbow.api.service;

import com.rainbow.api.dto.EbookRequestDTO;
import com.rainbow.api.dto.EbookResponseDTO;
import com.rainbow.api.entity.Ebook;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.IEbookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EbookService implements IEbookService {

    private final IEbookRepository ebookRepository;

    public EbookService(IEbookRepository ebookRepository) {
        this.ebookRepository = ebookRepository;
    }

    @Override
    public List<EbookResponseDTO> findAll() {
        return ebookRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public EbookResponseDTO findById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    public EbookResponseDTO create(EbookRequestDTO requestDTO) {
        Ebook ebook = new Ebook();
        applyRequest(ebook, requestDTO);
        return toResponse(ebookRepository.save(ebook));
    }

    @Override
    public EbookResponseDTO update(Long id, EbookRequestDTO requestDTO) {
        Ebook ebook = findByIdOrThrow(id);
        applyRequest(ebook, requestDTO);
        return toResponse(ebookRepository.save(ebook));
    }

    @Override
    public void deleteById(Long id) {
        Ebook ebook = findByIdOrThrow(id);
        ebookRepository.delete(ebook);
    }

    private Ebook findByIdOrThrow(Long id) {
        return ebookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ebook not found with id: " + id));
    }

    private void applyRequest(Ebook ebook, EbookRequestDTO requestDTO) {
        ebook.setTitle(requestDTO.title());
        ebook.setDescription(requestDTO.description());
        ebook.setPrice(requestDTO.price());
        ebook.setFilePath(requestDTO.filePath());
        ebook.setCoverUrl(requestDTO.coverUrl());
    }

    private EbookResponseDTO toResponse(Ebook ebook) {
        return new EbookResponseDTO(
                ebook.getId(),
                ebook.getTitle(),
                ebook.getDescription(),
                ebook.getPrice(),
                ebook.getFilePath(),
                ebook.getCoverUrl()
        );
    }
}
