package com.rainbow.api.controller;

import com.rainbow.api.dto.QuizQuestionRequestDTO;
import com.rainbow.api.dto.QuizQuestionResponseDTO;
import com.rainbow.api.service.IQuizQuestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-questions")
public class QuizQuestionController {

    private final IQuizQuestionService quizQuestionService;

    public QuizQuestionController(IQuizQuestionService quizQuestionService) {
        this.quizQuestionService = quizQuestionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<List<QuizQuestionResponseDTO>> getAll() {
        return ResponseEntity.ok(quizQuestionService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<QuizQuestionResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(quizQuestionService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<QuizQuestionResponseDTO> create(@Valid @RequestBody QuizQuestionRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizQuestionService.create(requestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<QuizQuestionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody QuizQuestionRequestDTO requestDTO) {
        return ResponseEntity.ok(quizQuestionService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        quizQuestionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
