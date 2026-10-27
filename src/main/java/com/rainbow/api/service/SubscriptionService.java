package com.rainbow.api.service;

import com.rainbow.api.dto.SubscriptionRequestDTO;
import com.rainbow.api.dto.SubscriptionResponseDTO;
import com.rainbow.api.entity.Course;
import com.rainbow.api.entity.Subscription;
import com.rainbow.api.entity.UserSec;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.ICourseRepository;
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

    public SubscriptionService(ISubscriptionRepository subscriptionRepository, IUserRepository userRepository, ICourseRepository courseRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
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
        return toResponse(subscriptionRepository.save(subscription));
    }

    @Override
    public SubscriptionResponseDTO update(Long id, SubscriptionRequestDTO requestDTO) {
        Subscription subscription = findByIdOrThrow(id);
        applyRequest(subscription, requestDTO);
        return toResponse(subscriptionRepository.save(subscription));
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
