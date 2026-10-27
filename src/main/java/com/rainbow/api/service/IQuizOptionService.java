package com.rainbow.api.service;

import com.rainbow.api.dto.QuizOptionRequestDTO;
import com.rainbow.api.dto.QuizOptionResponseDTO;

import java.util.List;

public interface IQuizOptionService {
    List<QuizOptionResponseDTO> findAll();
    QuizOptionResponseDTO findById(Long id);
    QuizOptionResponseDTO create(QuizOptionRequestDTO requestDTO);
    QuizOptionResponseDTO update(Long id, QuizOptionRequestDTO requestDTO);
    void deleteById(Long id);
}
