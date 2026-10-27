package com.rainbow.api.controller;

import com.rainbow.api.dto.ActivityResponseDTO;
import com.rainbow.api.dto.CompleteLessonRequestDTO;
import com.rainbow.api.dto.CompleteLessonResponseDTO;
import com.rainbow.api.dto.LessonRequestDTO;
import com.rainbow.api.dto.LessonResponseDTO;
import com.rainbow.api.entity.Activity;
import com.rainbow.api.entity.Lesson;
import com.rainbow.api.entity.StudentProgress;
import com.rainbow.api.entity.StudentProgressStatus;
import com.rainbow.api.entity.UserSec;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.IActivityRepository;
import com.rainbow.api.repository.ILessonRepository;
import com.rainbow.api.repository.IStudentProgressRepository;
import com.rainbow.api.repository.IUserRepository;
import com.rainbow.api.service.IActivityService;
import com.rainbow.api.service.ILessonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final ILessonService lessonService;
    private final IActivityService activityService;
    private final ILessonRepository lessonRepository;
    private final IActivityRepository activityRepository;
    private final IStudentProgressRepository studentProgressRepository;
    private final IUserRepository userRepository;

    public LessonController(ILessonService lessonService,
                           IActivityService activityService,
                           ILessonRepository lessonRepository,
                           IActivityRepository activityRepository,
                           IStudentProgressRepository studentProgressRepository,
                           IUserRepository userRepository) {
        this.lessonService = lessonService;
        this.activityService = activityService;
        this.lessonRepository = lessonRepository;
        this.activityRepository = activityRepository;
        this.studentProgressRepository = studentProgressRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<List<LessonResponseDTO>> getAll() {
        return ResponseEntity.ok(lessonService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<LessonResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.findById(id));
    }

    @GetMapping("/{lessonId}/activity")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<ActivityResponseDTO> getActivityByLessonId(@PathVariable Long lessonId) {
        return ResponseEntity.ok(activityService.findByLessonId(lessonId));
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<LessonResponseDTO> create(@Valid @RequestBody LessonRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.create(requestDTO));
    }

    @PostMapping("/{lessonId}/complete")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<CompleteLessonResponseDTO> completeLesson(@PathVariable Long lessonId,
                                                                   @RequestParam(required = false) Long studentId,
                                                                   @Valid @RequestBody CompleteLessonRequestDTO requestDTO) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + lessonId));

        Long resolvedStudentId = studentId != null ? studentId : resolveAuthenticatedStudentId();
        UserSec student = userRepository.findById(resolvedStudentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + resolvedStudentId));

        Activity activity = activityRepository.findByLessonId(lessonId).orElse(null);
        Integer minPassScore = activity != null ? activity.getMinPassScore() : 0;

        if (requestDTO.score() < minPassScore) {
            return ResponseEntity.ok(new CompleteLessonResponseDTO(
                    lessonId,
                    StudentProgressStatus.UNLOCKED,
                    requestDTO.score(),
                    false,
                    null,
                    false
            ));
        }

        StudentProgress progress = studentProgressRepository.findByStudentIdAndLessonId(resolvedStudentId, lessonId)
                .orElseGet(() -> StudentProgress.builder()
                        .student(student)
                        .lesson(lesson)
                        .status(StudentProgressStatus.UNLOCKED)
                        .score(0)
                        .build());

        progress.setStatus(StudentProgressStatus.COMPLETED);
        progress.setScore(requestDTO.score());
        progress.setCompletedAt(LocalDateTime.now());
        studentProgressRepository.save(progress);

        Lesson nextLesson = lessonRepository.findFirstByCourseIdAndOrderIndexGreaterThanOrderByOrderIndexAsc(
                lesson.getCourse().getId(),
                lesson.getOrderIndex()
        ).orElse(null);

        Long unlockedNextLessonId = null;
        boolean courseCompleted = false;
        if (nextLesson != null) {
            StudentProgress nextProgress = studentProgressRepository.findByStudentIdAndLessonId(resolvedStudentId, nextLesson.getId())
                    .orElseGet(() -> StudentProgress.builder()
                            .student(student)
                            .lesson(nextLesson)
                            .status(StudentProgressStatus.LOCKED)
                            .score(0)
                            .build());
            if (nextProgress.getStatus() == StudentProgressStatus.LOCKED) {
                nextProgress.setStatus(StudentProgressStatus.UNLOCKED);
                studentProgressRepository.save(nextProgress);
            }
            unlockedNextLessonId = nextLesson.getId();
        } else {
            courseCompleted = true;
        }

        return ResponseEntity.ok(new CompleteLessonResponseDTO(
                lessonId,
                StudentProgressStatus.COMPLETED,
                requestDTO.score(),
                true,
                unlockedNextLessonId,
                courseCompleted
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<LessonResponseDTO> update(@PathVariable Long id, @Valid @RequestBody LessonRequestDTO requestDTO) {
        return ResponseEntity.ok(lessonService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lessonService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Long resolveAuthenticatedStudentId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado: " + username))
                .getId();
    }
}
