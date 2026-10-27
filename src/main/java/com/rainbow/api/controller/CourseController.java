package com.rainbow.api.controller;

import com.rainbow.api.dto.CourseRequestDTO;
import com.rainbow.api.dto.CourseResponseDTO;
import com.rainbow.api.dto.CourseRoadmapActivityDTO;
import com.rainbow.api.dto.CourseRoadmapLessonDTO;
import com.rainbow.api.entity.Activity;
import com.rainbow.api.entity.Lesson;
import com.rainbow.api.entity.StudentProgress;
import com.rainbow.api.entity.UserSec;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.IActivityRepository;
import com.rainbow.api.repository.ILessonRepository;
import com.rainbow.api.repository.IStudentProgressRepository;
import com.rainbow.api.repository.IUserRepository;
import com.rainbow.api.service.ICourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final ICourseService courseService;
    private final ILessonRepository lessonRepository;
    private final IActivityRepository activityRepository;
    private final IStudentProgressRepository studentProgressRepository;
    private final IUserRepository userRepository;

    public CourseController(ICourseService courseService,
                           ILessonRepository lessonRepository,
                           IActivityRepository activityRepository,
                           IStudentProgressRepository studentProgressRepository,
                           IUserRepository userRepository) {
        this.courseService = courseService;
        this.lessonRepository = lessonRepository;
        this.activityRepository = activityRepository;
        this.studentProgressRepository = studentProgressRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<List<CourseResponseDTO>> getAll() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<CourseResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseResponseDTO> create(@Valid @RequestBody CourseRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(requestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CourseRequestDTO requestDTO) {
        return ResponseEntity.ok(courseService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{courseId}/roadmap")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<List<CourseRoadmapLessonDTO>> getRoadmap(@PathVariable Long courseId,
                                                                  @RequestParam(required = false) Long studentId) {
        Long resolvedStudentId = studentId != null ? studentId : resolveAuthenticatedStudentId();
        UserSec student = userRepository.findById(resolvedStudentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + resolvedStudentId));

        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
        List<CourseRoadmapLessonDTO> roadmap = lessons.stream()
                .map(lesson -> {
                    StudentProgress progress = studentProgressRepository.findByStudentIdAndLessonId(student.getId(), lesson.getId())
                            .orElseGet(() -> StudentProgress.builder()
                                    .student(student)
                                    .lesson(lesson)
                                    .status(com.rainbow.api.entity.StudentProgressStatus.LOCKED)
                                    .score(0)
                                    .build());

                    Activity activity = activityRepository.findByLessonId(lesson.getId()).orElse(null);
                    CourseRoadmapActivityDTO activitySummary = activity == null ? null :
                            new CourseRoadmapActivityDTO(activity.getId(), activity.getType(), activity.getTitle(), activity.getMinPassScore());

                    return new CourseRoadmapLessonDTO(
                            lesson.getId(),
                            lesson.getOrderIndex(),
                            lesson.getTitle(),
                            lesson.getDescription(),
                            progress.getStatus(),
                            progress.getScore(),
                            activitySummary
                    );
                })
                .toList();

        return ResponseEntity.ok(roadmap);
    }

    private Long resolveAuthenticatedStudentId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado: " + username))
                .getId();
    }
}
