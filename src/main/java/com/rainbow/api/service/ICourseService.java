package com.rainbow.api.service;

import com.rainbow.api.dto.CourseRequestDTO;
import com.rainbow.api.dto.CourseResponseDTO;

import java.util.List;

public interface ICourseService {
    List<CourseResponseDTO> findAll();
    CourseResponseDTO findById(Long id);
    CourseResponseDTO create(CourseRequestDTO requestDTO);
    CourseResponseDTO update(Long id, CourseRequestDTO requestDTO);
    void deleteById(Long id);
}
