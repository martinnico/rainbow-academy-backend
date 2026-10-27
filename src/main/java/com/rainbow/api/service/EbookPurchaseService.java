package com.rainbow.api.service;

import com.rainbow.api.dto.EbookPurchaseRequestDTO;
import com.rainbow.api.dto.EbookPurchaseResponseDTO;
import com.rainbow.api.entity.Ebook;
import com.rainbow.api.entity.EbookPurchase;
import com.rainbow.api.entity.UserSec;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.IEbookPurchaseRepository;
import com.rainbow.api.repository.IEbookRepository;
import com.rainbow.api.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EbookPurchaseService implements IEbookPurchaseService {

    private final IEbookPurchaseRepository ebookPurchaseRepository;
    private final IUserRepository userRepository;
    private final IEbookRepository ebookRepository;

    public EbookPurchaseService(IEbookPurchaseRepository ebookPurchaseRepository, IUserRepository userRepository, IEbookRepository ebookRepository) {
        this.ebookPurchaseRepository = ebookPurchaseRepository;
        this.userRepository = userRepository;
        this.ebookRepository = ebookRepository;
    }

    @Override
    public List<EbookPurchaseResponseDTO> findAll() {
        return ebookPurchaseRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public EbookPurchaseResponseDTO findById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    public EbookPurchaseResponseDTO create(EbookPurchaseRequestDTO requestDTO) {
        EbookPurchase ebookPurchase = new EbookPurchase();
        applyRequest(ebookPurchase, requestDTO);
        return toResponse(ebookPurchaseRepository.save(ebookPurchase));
    }

    @Override
    public EbookPurchaseResponseDTO update(Long id, EbookPurchaseRequestDTO requestDTO) {
        EbookPurchase ebookPurchase = findByIdOrThrow(id);
        applyRequest(ebookPurchase, requestDTO);
        return toResponse(ebookPurchaseRepository.save(ebookPurchase));
    }

    @Override
    public void deleteById(Long id) {
        EbookPurchase ebookPurchase = findByIdOrThrow(id);
        ebookPurchaseRepository.delete(ebookPurchase);
    }

    private EbookPurchase findByIdOrThrow(Long id) {
        return ebookPurchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EbookPurchase not found with id: " + id));
    }

    private UserSec findStudentByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private Ebook findEbookByIdOrThrow(Long id) {
        return ebookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ebook not found with id: " + id));
    }

    private void applyRequest(EbookPurchase ebookPurchase, EbookPurchaseRequestDTO requestDTO) {
        ebookPurchase.setStudent(findStudentByIdOrThrow(requestDTO.studentId()));
        ebookPurchase.setEbook(findEbookByIdOrThrow(requestDTO.ebookId()));
        ebookPurchase.setPurchaseDate(requestDTO.purchaseDate());
        ebookPurchase.setAmountPaid(requestDTO.amountPaid());
    }

    private EbookPurchaseResponseDTO toResponse(EbookPurchase ebookPurchase) {
        return new EbookPurchaseResponseDTO(
                ebookPurchase.getId(),
                ebookPurchase.getStudent().getId(),
                ebookPurchase.getEbook().getId(),
                ebookPurchase.getPurchaseDate(),
                ebookPurchase.getAmountPaid()
        );
    }
}
