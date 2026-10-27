package com.rainbow.api.service;

import com.rainbow.api.dto.ActivityQuizOptionRequestDTO;
import com.rainbow.api.dto.ActivityQuizOptionResponseDTO;
import com.rainbow.api.dto.ActivityQuizQuestionRequestDTO;
import com.rainbow.api.dto.ActivityQuizQuestionResponseDTO;
import com.rainbow.api.dto.ActivityRequestDTO;
import com.rainbow.api.dto.ActivityResponseDTO;
import com.rainbow.api.entity.Activity;
import com.rainbow.api.entity.ActivityType;
import com.rainbow.api.entity.Lesson;
import com.rainbow.api.entity.QuizOption;
import com.rainbow.api.entity.QuizQuestion;
import com.rainbow.api.entity.WordSearchWord;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.IActivityRepository;
import com.rainbow.api.repository.IQuizOptionRepository;
import com.rainbow.api.repository.IQuizQuestionRepository;
import com.rainbow.api.repository.ILessonRepository;
import com.rainbow.api.repository.IWordSearchWordRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ActivityService implements IActivityService {

    private final IActivityRepository activityRepository;
    private final ILessonRepository lessonRepository;
    private final IWordSearchWordRepository wordSearchWordRepository;
    private final IQuizQuestionRepository quizQuestionRepository;
    private final IQuizOptionRepository quizOptionRepository;

    public ActivityService(IActivityRepository activityRepository,
                           ILessonRepository lessonRepository,
                           IWordSearchWordRepository wordSearchWordRepository,
                           IQuizQuestionRepository quizQuestionRepository,
                           IQuizOptionRepository quizOptionRepository) {
        this.activityRepository = activityRepository;
        this.lessonRepository = lessonRepository;
        this.wordSearchWordRepository = wordSearchWordRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizOptionRepository = quizOptionRepository;
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
    public ActivityResponseDTO findByLessonId(Long lessonId) {
        Activity activity = activityRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found for lesson id: " + lessonId));
        return toResponse(activity);
    }

    @Override
    public ActivityResponseDTO create(ActivityRequestDTO requestDTO) {
        Activity activity = new Activity();
        applyRequest(activity, requestDTO);
        Activity savedActivity = activityRepository.save(activity);
        persistNestedData(savedActivity, requestDTO);
        return toResponse(savedActivity);
    }

    @Override
    public ActivityResponseDTO update(Long id, ActivityRequestDTO requestDTO) {
        Activity activity = findByIdOrThrow(id);
        applyRequest(activity, requestDTO);
        Activity savedActivity = activityRepository.save(activity);
        persistNestedData(savedActivity, requestDTO);
        return toResponse(savedActivity);
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

    private void persistNestedData(Activity activity, ActivityRequestDTO requestDTO) {
        List<WordSearchWord> existingWords = wordSearchWordRepository.findByActivityIdOrderByIdAsc(activity.getId());
        if (!existingWords.isEmpty()) {
            wordSearchWordRepository.deleteAll(existingWords);
        }

        List<QuizQuestion> existingQuestions = quizQuestionRepository.findByActivityIdOrderByOrderIndexAsc(activity.getId());
        if (!existingQuestions.isEmpty()) {
            for (QuizQuestion question : existingQuestions) {
                quizOptionRepository.deleteAll(quizOptionRepository.findByQuestionIdOrderByIdAsc(question.getId()));
            }
            quizQuestionRepository.deleteAll(existingQuestions);
        }

        if (requestDTO.type() == ActivityType.WORD_SEARCH) {
            if (requestDTO.words() != null) {
                for (String word : requestDTO.words()) {
                    if (word != null && !word.isBlank()) {
                        wordSearchWordRepository.save(WordSearchWord.builder()
                                .activity(activity)
                                .word(word.trim())
                                .build());
                    }
                }
            }
            return;
        }

        if (requestDTO.type() == ActivityType.QUIZ) {
            if (requestDTO.questions() != null) {
                for (ActivityQuizQuestionRequestDTO questionDTO : requestDTO.questions()) {
                    if (questionDTO == null) {
                        continue;
                    }
                    QuizQuestion question = quizQuestionRepository.save(QuizQuestion.builder()
                            .activity(activity)
                            .questionText(questionDTO.questionText().trim())
                            .orderIndex(questionDTO.orderIndex())
                            .build());
                    if (questionDTO.options() != null) {
                        for (ActivityQuizOptionRequestDTO optionDTO : questionDTO.options()) {
                            if (optionDTO == null) {
                                continue;
                            }
                            quizOptionRepository.save(QuizOption.builder()
                                    .question(question)
                                    .optionText(optionDTO.optionText().trim())
                                    .isCorrect(optionDTO.isCorrect())
                                    .build());
                        }
                    }
                }
            }
        }
    }

    private ActivityResponseDTO toResponse(Activity activity) {
        List<String> words = wordSearchWordRepository.findByActivityIdOrderByIdAsc(activity.getId()).stream()
                .map(WordSearchWord::getWord)
                .toList();

        List<ActivityQuizQuestionResponseDTO> questions = quizQuestionRepository.findByActivityIdOrderByOrderIndexAsc(activity.getId()).stream()
                .map(question -> {
                    List<ActivityQuizOptionResponseDTO> options = quizOptionRepository.findByQuestionIdOrderByIdAsc(question.getId()).stream()
                            .map(option -> new ActivityQuizOptionResponseDTO(option.getId(), option.getOptionText(), option.isCorrect()))
                            .toList();
                    return new ActivityQuizQuestionResponseDTO(question.getId(), question.getQuestionText(), question.getOrderIndex(), options);
                })
                .toList();

        return new ActivityResponseDTO(
                activity.getId(),
                activity.getLesson().getId(),
                activity.getType(),
                activity.getTitle(),
                activity.getMinPassScore(),
                words,
                questions
        );
    }
}
