package com.rainbow.api.service;

import com.rainbow.api.dto.WordSearchWordRequestDTO;
import com.rainbow.api.dto.WordSearchWordResponseDTO;

import java.util.List;

public interface IWordSearchWordService {
    List<WordSearchWordResponseDTO> findAll();
    WordSearchWordResponseDTO findById(Long id);
    WordSearchWordResponseDTO create(WordSearchWordRequestDTO requestDTO);
    WordSearchWordResponseDTO update(Long id, WordSearchWordRequestDTO requestDTO);
    void deleteById(Long id);
}
