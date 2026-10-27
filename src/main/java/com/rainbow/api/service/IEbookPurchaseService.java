package com.rainbow.api.service;

import com.rainbow.api.dto.EbookPurchaseRequestDTO;
import com.rainbow.api.dto.EbookPurchaseResponseDTO;

import java.util.List;

public interface IEbookPurchaseService {
    List<EbookPurchaseResponseDTO> findAll();
    EbookPurchaseResponseDTO findById(Long id);
    EbookPurchaseResponseDTO create(EbookPurchaseRequestDTO requestDTO);
    EbookPurchaseResponseDTO update(Long id, EbookPurchaseRequestDTO requestDTO);
    void deleteById(Long id);
}
