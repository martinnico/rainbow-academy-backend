package com.rainbow.api.service;

import com.rainbow.api.dto.StudentProgressRequestDTO;
import com.rainbow.api.dto.StudentProgressResponseDTO;

import java.util.List;

public interface IStudentProgressService {
    List<StudentProgressResponseDTO> findAll();
    StudentProgressResponseDTO findById(Long id);
    StudentProgressResponseDTO create(StudentProgressRequestDTO requestDTO);
    StudentProgressResponseDTO update(Long id, StudentProgressRequestDTO requestDTO);
    void deleteById(Long id);
}
