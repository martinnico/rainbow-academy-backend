package com.rainbow.api.service;

import com.rainbow.api.dto.ActivityRequestDTO;
import com.rainbow.api.dto.ActivityResponseDTO;
import com.rainbow.api.entity.Activity;
import com.rainbow.api.entity.Lesson;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.IActivityRepository;
import com.rainbow.api.repository.ILessonRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ActivityService implements IActivityService {

    private final IActivityRepository activityRepository;
    private final ILessonRepository lessonRepository;

    public ActivityService(IActivityRepository activityRepository, ILessonRepository lessonRepository) {
        this.activityRepository = activityRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<ActivityResponseDTO> findAll() {
        return activityRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ActivityResponseDTO findById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    public ActivityResponseDTO create(ActivityRequestDTO requestDTO) {
        Activity activity = new Activity();
        applyRequest(activity, requestDTO);
        return toResponse(activityRepository.save(activity));
    }

    @Override
    public ActivityResponseDTO update(Long id, ActivityRequestDTO requestDTO) {
        Activity activity = findByIdOrThrow(id);
        applyRequest(activity, requestDTO);
        return toResponse(activityRepository.save(activity));
    }

    @Override
    public void deleteById(Long id) {
        Activity activity = findByIdOrThrow(id);
        activityRepository.delete(activity);
    }

    private Activity findByIdOrThrow(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id: " + id));
    }

    private Lesson findLessonByIdOrThrow(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
    }

    private void applyRequest(Activity activity, ActivityRequestDTO requestDTO) {
        activity.setLesson(findLessonByIdOrThrow(requestDTO.lessonId()));
        activity.setType(requestDTO.type());
        activity.setTitle(requestDTO.title());
        activity.setMinPassScore(requestDTO.minPassScore());
    }

    private ActivityResponseDTO toResponse(Activity activity) {
        return new ActivityResponseDTO(
                activity.getId(),
                activity.getLesson().getId(),
                activity.getType(),
                activity.getTitle(),
                activity.getMinPassScore()
        );
    }
}
