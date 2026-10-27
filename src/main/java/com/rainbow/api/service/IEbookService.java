package com.rainbow.api.service;

import com.rainbow.api.dto.EbookRequestDTO;
import com.rainbow.api.dto.EbookResponseDTO;

import java.util.List;

public interface IEbookService {
    List<EbookResponseDTO> findAll();
    EbookResponseDTO findById(Long id);
    EbookResponseDTO create(EbookRequestDTO requestDTO);
    EbookResponseDTO update(Long id, EbookRequestDTO requestDTO);
    void deleteById(Long id);
}
