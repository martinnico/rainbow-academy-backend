package com.rainbow.api.service;

import com.rainbow.api.dto.StudentProgressRequestDTO;
import com.rainbow.api.dto.StudentProgressResponseDTO;
import com.rainbow.api.entity.Lesson;
import com.rainbow.api.entity.StudentProgress;
import com.rainbow.api.entity.UserSec;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.ILessonRepository;
import com.rainbow.api.repository.IStudentProgressRepository;
import com.rainbow.api.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class StudentProgressService implements IStudentProgressService {

    private final IStudentProgressRepository studentProgressRepository;
    private final IUserRepository userRepository;
    private final ILessonRepository lessonRepository;

    public StudentProgressService(IStudentProgressRepository studentProgressRepository, IUserRepository userRepository, ILessonRepository lessonRepository) {
        this.studentProgressRepository = studentProgressRepository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<StudentProgressResponseDTO> findAll() {
        return studentProgressRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public StudentProgressResponseDTO findById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    public StudentProgressResponseDTO create(StudentProgressRequestDTO requestDTO) {
        StudentProgress studentProgress = new StudentProgress();
        applyRequest(studentProgress, requestDTO);
        return toResponse(studentProgressRepository.save(studentProgress));
    }

    @Override
    public StudentProgressResponseDTO update(Long id, StudentProgressRequestDTO requestDTO) {
        StudentProgress studentProgress = findByIdOrThrow(id);
        applyRequest(studentProgress, requestDTO);
        return toResponse(studentProgressRepository.save(studentProgress));
    }

    @Override
    public void deleteById(Long id) {
        StudentProgress studentProgress = findByIdOrThrow(id);
        studentProgressRepository.delete(studentProgress);
    }

    private StudentProgress findByIdOrThrow(Long id) {
        return studentProgressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StudentProgress not found with id: " + id));
    }

    private UserSec findStudentByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private Lesson findLessonByIdOrThrow(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
    }

    private void applyRequest(StudentProgress studentProgress, StudentProgressRequestDTO requestDTO) {
        studentProgress.setStudent(findStudentByIdOrThrow(requestDTO.studentId()));
        studentProgress.setLesson(findLessonByIdOrThrow(requestDTO.lessonId()));
        studentProgress.setStatus(requestDTO.status());
        studentProgress.setScore(requestDTO.score());
        studentProgress.setCompletedAt(requestDTO.completedAt());
    }

    private StudentProgressResponseDTO toResponse(StudentProgress studentProgress) {
        return new StudentProgressResponseDTO(
                studentProgress.getId(),
                studentProgress.getStudent().getId(),
                studentProgress.getLesson().getId(),
                studentProgress.getStatus(),
                studentProgress.getScore(),
                studentProgress.getCompletedAt()
        );
    }
}
