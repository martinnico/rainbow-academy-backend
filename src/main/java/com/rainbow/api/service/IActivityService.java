package com.rainbow.api.service;

import com.rainbow.api.dto.ActivityRequestDTO;
import com.rainbow.api.dto.ActivityResponseDTO;

import java.util.List;

public interface IActivityService {
    List<ActivityResponseDTO> findAll();
    ActivityResponseDTO findById(Long id);
    ActivityResponseDTO findByLessonId(Long lessonId);
    ActivityResponseDTO create(ActivityRequestDTO requestDTO);
    ActivityResponseDTO update(Long id, ActivityRequestDTO requestDTO);
    void deleteById(Long id);
}
