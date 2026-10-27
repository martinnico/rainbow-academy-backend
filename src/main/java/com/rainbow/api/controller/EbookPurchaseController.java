package com.rainbow.api.controller;

import com.rainbow.api.dto.EbookPurchaseRequestDTO;
import com.rainbow.api.dto.EbookPurchaseResponseDTO;
import com.rainbow.api.service.IEbookPurchaseService;
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
@RequestMapping("/api/ebook-purchases")
public class EbookPurchaseController {

    private final IEbookPurchaseService ebookPurchaseService;

    public EbookPurchaseController(IEbookPurchaseService ebookPurchaseService) {
        this.ebookPurchaseService = ebookPurchaseService;
    }

    @GetMapping
    public ResponseEntity<List<EbookPurchaseResponseDTO>> getAll() {
        return ResponseEntity.ok(ebookPurchaseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EbookPurchaseResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ebookPurchaseService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EbookPurchaseResponseDTO> create(@Valid @RequestBody EbookPurchaseRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ebookPurchaseService.create(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EbookPurchaseResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EbookPurchaseRequestDTO requestDTO) {
        return ResponseEntity.ok(ebookPurchaseService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ebookPurchaseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
