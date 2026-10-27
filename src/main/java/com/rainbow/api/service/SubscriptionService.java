package com.rainbow.api.service;

import com.rainbow.api.dto.SubscriptionRequestDTO;
import com.rainbow.api.dto.SubscriptionResponseDTO;
import com.rainbow.api.entity.Course;
import com.rainbow.api.entity.Lesson;
import com.rainbow.api.entity.StudentProgress;
import com.rainbow.api.entity.StudentProgressStatus;
import com.rainbow.api.entity.Subscription;
import com.rainbow.api.entity.SubscriptionStatus;
import com.rainbow.api.entity.UserSec;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.ICourseRepository;
import com.rainbow.api.repository.ILessonRepository;
import com.rainbow.api.repository.IStudentProgressRepository;
import com.rainbow.api.repository.ISubscriptionRepository;
import com.rainbow.api.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SubscriptionService implements ISubscriptionService {

    private final ISubscriptionRepository subscriptionRepository;
    private final IUserRepository userRepository;
    private final ICourseRepository courseRepository;
    private final ILessonRepository lessonRepository;
    private final IStudentProgressRepository studentProgressRepository;

    public SubscriptionService(ISubscriptionRepository subscriptionRepository,
                              IUserRepository userRepository,
                              ICourseRepository courseRepository,
                              ILessonRepository lessonRepository,
                              IStudentProgressRepository studentProgressRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.studentProgressRepository = studentProgressRepository;
    }

    @Override
    public List<SubscriptionResponseDTO> findAll() {
        return subscriptionRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public SubscriptionResponseDTO findById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    public SubscriptionResponseDTO create(SubscriptionRequestDTO requestDTO) {
        Subscription subscription = new Subscription();
        applyRequest(subscription, requestDTO);
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        if (savedSubscription.getStatus() == SubscriptionStatus.ACTIVE) {
            initializeCourseProgress(savedSubscription.getStudent(), savedSubscription.getCourse().getId());
        }

        return toResponse(savedSubscription);
    }

    @Override
    public SubscriptionResponseDTO update(Long id, SubscriptionRequestDTO requestDTO) {
        Subscription subscription = findByIdOrThrow(id);
        applyRequest(subscription, requestDTO);
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        if (savedSubscription.getStatus() == SubscriptionStatus.ACTIVE) {
            initializeCourseProgress(savedSubscription.getStudent(), savedSubscription.getCourse().getId());
        }

        return toResponse(savedSubscription);
    }

    @Override
    public void deleteById(Long id) {
        Subscription subscription = findByIdOrThrow(id);
        subscriptionRepository.delete(subscription);
    }

    private Subscription findByIdOrThrow(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + id));
    }

    private UserSec findStudentByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private Course findCourseByIdOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private void initializeCourseProgress(UserSec student, Long courseId) {
        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByOrderIndexAsc(courseId);
        if (lessons.isEmpty()) {
            return;
        }

        Lesson firstLesson = lessons.get(0);
        ensureProgress(student, firstLesson, StudentProgressStatus.UNLOCKED);

        for (int i = 1; i < lessons.size(); i++) {
            ensureProgress(student, lessons.get(i), StudentProgressStatus.LOCKED);
        }
    }

    private void ensureProgress(UserSec student, Lesson lesson, StudentProgressStatus status) {
        StudentProgress progress = studentProgressRepository.findByStudentIdAndLessonId(student.getId(), lesson.getId())
                .orElseGet(() -> StudentProgress.builder()
                        .student(student)
                        .lesson(lesson)
                        .status(StudentProgressStatus.LOCKED)
                        .score(0)
                        .build());

        progress.setStatus(status);
        studentProgressRepository.save(progress);
    }

    private void applyRequest(Subscription subscription, SubscriptionRequestDTO requestDTO) {
        if (requestDTO.endDate().isBefore(requestDTO.startDate())) {
            throw new IllegalArgumentException("endDate must be equal or after startDate");
        }
        subscription.setStudent(findStudentByIdOrThrow(requestDTO.studentId()));
        subscription.setCourse(findCourseByIdOrThrow(requestDTO.courseId()));
        subscription.setStatus(requestDTO.status());
        subscription.setStartDate(requestDTO.startDate());
        subscription.setEndDate(requestDTO.endDate());
        subscription.setAmountPaid(requestDTO.amountPaid());
    }

    private SubscriptionResponseDTO toResponse(Subscription subscription) {
        return new SubscriptionResponseDTO(
                subscription.getId(),
                subscription.getStudent().getId(),
                subscription.getCourse().getId(),
                subscription.getStatus(),
                subscription.getStartDate(),
                subscription.getEndDate(),
                subscription.getAmountPaid()
        );
    }
}
