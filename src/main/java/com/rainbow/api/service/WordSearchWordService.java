package com.rainbow.api.service;

import com.rainbow.api.dto.WordSearchWordRequestDTO;
import com.rainbow.api.dto.WordSearchWordResponseDTO;
import com.rainbow.api.entity.Activity;
import com.rainbow.api.entity.WordSearchWord;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.IActivityRepository;
import com.rainbow.api.repository.IWordSearchWordRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WordSearchWordService implements IWordSearchWordService {

    private final IWordSearchWordRepository wordSearchWordRepository;
    private final IActivityRepository activityRepository;

    public WordSearchWordService(IWordSearchWordRepository wordSearchWordRepository, IActivityRepository activityRepository) {
        this.wordSearchWordRepository = wordSearchWordRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public List<WordSearchWordResponseDTO> findAll() {
        return wordSearchWordRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public WordSearchWordResponseDTO findById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Override
    public WordSearchWordResponseDTO create(WordSearchWordRequestDTO requestDTO) {
        WordSearchWord wordSearchWord = new WordSearchWord();
        applyRequest(wordSearchWord, requestDTO);
        return toResponse(wordSearchWordRepository.save(wordSearchWord));
    }

    @Override
    public WordSearchWordResponseDTO update(Long id, WordSearchWordRequestDTO requestDTO) {
        WordSearchWord wordSearchWord = findByIdOrThrow(id);
        applyRequest(wordSearchWord, requestDTO);
        return toResponse(wordSearchWordRepository.save(wordSearchWord));
    }

    @Override
    public void deleteById(Long id) {
        WordSearchWord wordSearchWord = findByIdOrThrow(id);
        wordSearchWordRepository.delete(wordSearchWord);
    }

    private WordSearchWord findByIdOrThrow(Long id) {
        return wordSearchWordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WordSearchWord not found with id: " + id));
    }

    private Activity findActivityByIdOrThrow(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id: " + id));
    }

    private void applyRequest(WordSearchWord wordSearchWord, WordSearchWordRequestDTO requestDTO) {
        wordSearchWord.setActivity(findActivityByIdOrThrow(requestDTO.activityId()));
        wordSearchWord.setWord(requestDTO.word());
    }

    private WordSearchWordResponseDTO toResponse(WordSearchWord wordSearchWord) {
        return new WordSearchWordResponseDTO(
                wordSearchWord.getId(),
                wordSearchWord.getActivity().getId(),
                wordSearchWord.getWord()
        );
    }
}
