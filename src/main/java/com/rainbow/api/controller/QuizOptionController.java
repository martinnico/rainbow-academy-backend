package com.rainbow.api.controller;

import com.rainbow.api.dto.QuizOptionRequestDTO;
import com.rainbow.api.dto.QuizOptionResponseDTO;
import com.rainbow.api.service.IQuizOptionService;
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
@RequestMapping("/api/quiz-options")
public class QuizOptionController {

    private final IQuizOptionService quizOptionService;

    public QuizOptionController(IQuizOptionService quizOptionService) {
        this.quizOptionService = quizOptionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<List<QuizOptionResponseDTO>> getAll() {
        return ResponseEntity.ok(quizOptionService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<QuizOptionResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(quizOptionService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<QuizOptionResponseDTO> create(@Valid @RequestBody QuizOptionRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizOptionService.create(requestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<QuizOptionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody QuizOptionRequestDTO requestDTO) {
        return ResponseEntity.ok(quizOptionService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        quizOptionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
