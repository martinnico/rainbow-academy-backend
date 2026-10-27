package com.rainbow.api.service;

import com.rainbow.api.dto.QuizOptionRequestDTO;
import com.rainbow.api.dto.QuizOptionResponseDTO;
import com.rainbow.api.entity.QuizOption;
import com.rainbow.api.entity.QuizQuestion;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.IQuizOptionRepository;
import com.rainbow.api.repository.IQuizQuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class QuizOptionService implements IQuizOptionService {

    private final IQuizOptionRepository quizOptionRepository;
    private final IQuizQuestionRepository quizQuestionRepository;

    public QuizOptionService(IQuizOptionRepository quizOptionRepository, IQuizQuestionRepository quizQuestionRepository) {
        this.quizOptionRepository = quizOptionRepository;
        this.quizQuestionRepository = quizQuestionRepository;
    }

    @Override
    public List<QuizOptionResponseDTO> findAll() {
        return quizOptionRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public QuizOptionResponseDTO findById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    public QuizOptionResponseDTO create(QuizOptionRequestDTO requestDTO) {
        QuizOption quizOption = new QuizOption();
        applyRequest(quizOption, requestDTO);
        return toResponse(quizOptionRepository.save(quizOption));
    }

    @Override
    public QuizOptionResponseDTO update(Long id, QuizOptionRequestDTO requestDTO) {
        QuizOption quizOption = findByIdOrThrow(id);
        applyRequest(quizOption, requestDTO);
        return toResponse(quizOptionRepository.save(quizOption));
    }

    @Override
    public void deleteById(Long id) {
        QuizOption quizOption = findByIdOrThrow(id);
        quizOptionRepository.delete(quizOption);
    }

    private QuizOption findByIdOrThrow(Long id) {
        return quizOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuizOption not found with id: " + id));
    }

    private QuizQuestion findQuestionByIdOrThrow(Long id) {
        return quizQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuizQuestion not found with id: " + id));
    }

    private void applyRequest(QuizOption quizOption, QuizOptionRequestDTO requestDTO) {
        quizOption.setQuestion(findQuestionByIdOrThrow(requestDTO.questionId()));
        quizOption.setOptionText(requestDTO.optionText());
        quizOption.setCorrect(requestDTO.isCorrect());
    }

    private QuizOptionResponseDTO toResponse(QuizOption quizOption) {
        return new QuizOptionResponseDTO(
                quizOption.getId(),
                quizOption.getQuestion().getId(),
                quizOption.getOptionText(),
                quizOption.isCorrect()
        );
    }
}
