package com.rainbow.api.controller;

import com.rainbow.api.dto.EbookRequestDTO;
import com.rainbow.api.dto.EbookResponseDTO;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.IEbookPurchaseRepository;
import com.rainbow.api.repository.IUserRepository;
import com.rainbow.api.service.IEbookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ebooks")
public class EbookController {

    private final IEbookService ebookService;
    private final IEbookPurchaseRepository ebookPurchaseRepository;
    private final IUserRepository userRepository;

    public EbookController(IEbookService ebookService,
                          IEbookPurchaseRepository ebookPurchaseRepository,
                          IUserRepository userRepository) {
        this.ebookService = ebookService;
        this.ebookPurchaseRepository = ebookPurchaseRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<List<EbookResponseDTO>> getAll() {
        return ResponseEntity.ok(ebookService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<EbookResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ebookService.findById(id));
    }

    @GetMapping("/{ebookId}/access")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<EbookResponseDTO> accessEbook(@PathVariable Long ebookId,
                                                       @RequestParam(required = false) Long studentId) {
        Long resolvedStudentId = studentId != null ? studentId : resolveAuthenticatedStudentId();
        boolean isTeacher = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_TEACHER"));

        if (!isTeacher && !ebookPurchaseRepository.existsByStudentIdAndEbookId(resolvedStudentId, ebookId)) {
            throw new AccessDeniedException("No tenés acceso a este ebook");
        }

        return ResponseEntity.ok(ebookService.findById(ebookId));
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<EbookResponseDTO> create(@Valid @RequestBody EbookRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ebookService.create(requestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<EbookResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EbookRequestDTO requestDTO) {
        return ResponseEntity.ok(ebookService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ebookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Long resolveAuthenticatedStudentId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado: " + username))
                .getId();
    }
}
