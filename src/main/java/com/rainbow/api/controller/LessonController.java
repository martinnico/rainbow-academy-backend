package com.rainbow.api.controller;

import com.rainbow.api.dto.LessonRequestDTO;
import com.rainbow.api.dto.LessonResponseDTO;
import com.rainbow.api.service.ILessonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/lessons")
public class LessonController {

    private final ILessonService lessonService;

    public LessonController(ILessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ResponseEntity<List<LessonResponseDTO>> getAll() {
        return ResponseEntity.ok(lessonService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LessonResponseDTO> create(@Valid @RequestBody LessonRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.create(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> update(@PathVariable Long id, @Valid @RequestBody LessonRequestDTO requestDTO) {
        return ResponseEntity.ok(lessonService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lessonService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
