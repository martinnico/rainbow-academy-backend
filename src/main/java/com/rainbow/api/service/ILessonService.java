package com.rainbow.api.service;

import com.rainbow.api.dto.LessonRequestDTO;
import com.rainbow.api.dto.LessonResponseDTO;

import java.util.List;

public interface ILessonService {
    List<LessonResponseDTO> findAll();
    LessonResponseDTO findById(Long id);
    LessonResponseDTO create(LessonRequestDTO requestDTO);
    LessonResponseDTO update(Long id, LessonRequestDTO requestDTO);
    void deleteById(Long id);
}
