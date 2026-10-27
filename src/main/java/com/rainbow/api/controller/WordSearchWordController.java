package com.rainbow.api.controller;

import com.rainbow.api.dto.WordSearchWordRequestDTO;
import com.rainbow.api.dto.WordSearchWordResponseDTO;
import com.rainbow.api.service.IWordSearchWordService;
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
@RequestMapping("/api/word-search-words")
public class WordSearchWordController {

    private final IWordSearchWordService wordSearchWordService;

    public WordSearchWordController(IWordSearchWordService wordSearchWordService) {
        this.wordSearchWordService = wordSearchWordService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<List<WordSearchWordResponseDTO>> getAll() {
        return ResponseEntity.ok(wordSearchWordService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<WordSearchWordResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(wordSearchWordService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<WordSearchWordResponseDTO> create(@Valid @RequestBody WordSearchWordRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wordSearchWordService.create(requestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<WordSearchWordResponseDTO> update(@PathVariable Long id, @Valid @RequestBody WordSearchWordRequestDTO requestDTO) {
        return ResponseEntity.ok(wordSearchWordService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        wordSearchWordService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
