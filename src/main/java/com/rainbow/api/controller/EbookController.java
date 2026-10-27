package com.rainbow.api.controller;

import com.rainbow.api.dto.EbookRequestDTO;
import com.rainbow.api.dto.EbookResponseDTO;
import com.rainbow.api.service.IEbookService;
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
@RequestMapping("/api/ebooks")
public class EbookController {

    private final IEbookService ebookService;

    public EbookController(IEbookService ebookService) {
        this.ebookService = ebookService;
    }

    @GetMapping
    public ResponseEntity<List<EbookResponseDTO>> getAll() {
        return ResponseEntity.ok(ebookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EbookResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ebookService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EbookResponseDTO> create(@Valid @RequestBody EbookRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ebookService.create(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EbookResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EbookRequestDTO requestDTO) {
        return ResponseEntity.ok(ebookService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ebookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
