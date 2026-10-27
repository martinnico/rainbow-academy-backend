package com.rainbow.api.service;

import com.rainbow.api.dto.LessonRequestDTO;
import com.rainbow.api.dto.LessonResponseDTO;
import com.rainbow.api.entity.Course;
import com.rainbow.api.entity.Lesson;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.ICourseRepository;
import com.rainbow.api.repository.ILessonRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LessonService implements ILessonService {

    private final ILessonRepository lessonRepository;
    private final ICourseRepository courseRepository;

    public LessonService(ILessonRepository lessonRepository, ICourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<LessonResponseDTO> findAll() {
        return lessonRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public LessonResponseDTO findById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    public LessonResponseDTO create(LessonRequestDTO requestDTO) {
        Lesson lesson = new Lesson();
        applyRequest(lesson, requestDTO);
        return toResponse(lessonRepository.save(lesson));
    }

    @Override
    public LessonResponseDTO update(Long id, LessonRequestDTO requestDTO) {
        Lesson lesson = findByIdOrThrow(id);
        applyRequest(lesson, requestDTO);
        return toResponse(lessonRepository.save(lesson));
    }

    @Override
    public void deleteById(Long id) {
        Lesson lesson = findByIdOrThrow(id);
        lessonRepository.delete(lesson);
    }

    private Lesson findByIdOrThrow(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
    }

    private Course findCourseByIdOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private void applyRequest(Lesson lesson, LessonRequestDTO requestDTO) {
        lesson.setCourse(findCourseByIdOrThrow(requestDTO.courseId()));
        lesson.setOrderIndex(requestDTO.orderIndex());
        lesson.setTitle(requestDTO.title());
        lesson.setVideoUrl(requestDTO.videoUrl());
        lesson.setDescription(requestDTO.description());
    }

    private LessonResponseDTO toResponse(Lesson lesson) {
        return new LessonResponseDTO(
                lesson.getId(),
                lesson.getCourse().getId(),
                lesson.getOrderIndex(),
                lesson.getTitle(),
                lesson.getVideoUrl(),
                lesson.getDescription()
        );
    }
}
