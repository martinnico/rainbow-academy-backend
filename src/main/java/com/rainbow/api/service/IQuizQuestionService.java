package com.rainbow.api.service;

import com.rainbow.api.dto.QuizQuestionRequestDTO;
import com.rainbow.api.dto.QuizQuestionResponseDTO;

import java.util.List;

public interface IQuizQuestionService {
    List<QuizQuestionResponseDTO> findAll();
    QuizQuestionResponseDTO findById(Long id);
    QuizQuestionResponseDTO create(QuizQuestionRequestDTO requestDTO);
    QuizQuestionResponseDTO update(Long id, QuizQuestionRequestDTO requestDTO);
    void deleteById(Long id);
}
