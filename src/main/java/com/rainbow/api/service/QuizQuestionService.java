package com.rainbow.api.service;

import com.rainbow.api.dto.QuizQuestionRequestDTO;
import com.rainbow.api.dto.QuizQuestionResponseDTO;
import com.rainbow.api.entity.Activity;
import com.rainbow.api.entity.QuizQuestion;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.IActivityRepository;
import com.rainbow.api.repository.IQuizQuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class QuizQuestionService implements IQuizQuestionService {

    private final IQuizQuestionRepository quizQuestionRepository;
    private final IActivityRepository activityRepository;

    public QuizQuestionService(IQuizQuestionRepository quizQuestionRepository, IActivityRepository activityRepository) {
        this.quizQuestionRepository = quizQuestionRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public List<QuizQuestionResponseDTO> findAll() {
        return quizQuestionRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public QuizQuestionResponseDTO findById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    public QuizQuestionResponseDTO create(QuizQuestionRequestDTO requestDTO) {
        QuizQuestion quizQuestion = new QuizQuestion();
        applyRequest(quizQuestion, requestDTO);
        return toResponse(quizQuestionRepository.save(quizQuestion));
    }

    @Override
    public QuizQuestionResponseDTO update(Long id, QuizQuestionRequestDTO requestDTO) {
        QuizQuestion quizQuestion = findByIdOrThrow(id);
        applyRequest(quizQuestion, requestDTO);
        return toResponse(quizQuestionRepository.save(quizQuestion));
    }

    @Override
    public void deleteById(Long id) {
        QuizQuestion quizQuestion = findByIdOrThrow(id);
        quizQuestionRepository.delete(quizQuestion);
    }

    private QuizQuestion findByIdOrThrow(Long id) {
        return quizQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuizQuestion not found with id: " + id));
    }

    private Activity findActivityByIdOrThrow(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id: " + id));
    }

    private void applyRequest(QuizQuestion quizQuestion, QuizQuestionRequestDTO requestDTO) {
        quizQuestion.setActivity(findActivityByIdOrThrow(requestDTO.activityId()));
        quizQuestion.setQuestionText(requestDTO.questionText());
        quizQuestion.setOrderIndex(requestDTO.orderIndex());
    }

    private QuizQuestionResponseDTO toResponse(QuizQuestion quizQuestion) {
        return new QuizQuestionResponseDTO(
                quizQuestion.getId(),
                quizQuestion.getActivity().getId(),
                quizQuestion.getQuestionText(),
                quizQuestion.getOrderIndex()
        );
    }
}
