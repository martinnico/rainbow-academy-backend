package com.rainbow.api.service;

import com.rainbow.api.dto.CourseRequestDTO;
import com.rainbow.api.dto.CourseResponseDTO;
import com.rainbow.api.entity.Course;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.ICourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CourseService implements ICourseService {

    private final ICourseRepository courseRepository;

    public CourseService(ICourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<CourseResponseDTO> findAll() {
        return courseRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public CourseResponseDTO findById(Long id) {
        return toResponse(findCourseByIdOrThrow(id));
    }

    @Override
    public CourseResponseDTO create(CourseRequestDTO requestDTO) {
        Course course = new Course();
        applyRequest(course, requestDTO);
        return toResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponseDTO update(Long id, CourseRequestDTO requestDTO) {
        Course course = findCourseByIdOrThrow(id);
        applyRequest(course, requestDTO);
        return toResponse(courseRepository.save(course));
    }

    @Override
    public void deleteById(Long id) {
        Course course = findCourseByIdOrThrow(id);
        courseRepository.delete(course);
    }

    private Course findCourseByIdOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private void applyRequest(Course course, CourseRequestDTO requestDTO) {
        course.setTitle(requestDTO.title());
        course.setDescription(requestDTO.description());
        course.setLevel(requestDTO.level());
        course.setMonthlyPrice(requestDTO.monthlyPrice());
        course.setActive(requestDTO.active());
    }

    private CourseResponseDTO toResponse(Course course) {
        return new CourseResponseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getLevel(),
                course.getMonthlyPrice(),
                course.isActive()
        );
    }
}
