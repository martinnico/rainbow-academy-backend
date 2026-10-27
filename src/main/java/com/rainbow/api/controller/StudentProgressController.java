package com.rainbow.api.controller;

import com.rainbow.api.dto.StudentProgressRequestDTO;
import com.rainbow.api.dto.StudentProgressResponseDTO;
import com.rainbow.api.service.IStudentProgressService;
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
@RequestMapping("/api/student-progress")
public class StudentProgressController {

    private final IStudentProgressService studentProgressService;

    public StudentProgressController(IStudentProgressService studentProgressService) {
        this.studentProgressService = studentProgressService;
    }

    @GetMapping
    public ResponseEntity<List<StudentProgressResponseDTO>> getAll() {
        return ResponseEntity.ok(studentProgressService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentProgressResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentProgressService.findById(id));
    }

    @PostMapping
    public ResponseEntity<StudentProgressResponseDTO> create(@Valid @RequestBody StudentProgressRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentProgressService.create(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentProgressResponseDTO> update(@PathVariable Long id, @Valid @RequestBody StudentProgressRequestDTO requestDTO) {
        return ResponseEntity.ok(studentProgressService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentProgressService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
