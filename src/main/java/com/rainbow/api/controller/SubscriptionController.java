package com.rainbow.api.controller;

import com.rainbow.api.dto.SubscriptionRequestDTO;
import com.rainbow.api.dto.SubscriptionResponseDTO;
import com.rainbow.api.service.ISubscriptionService;
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
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final ISubscriptionService subscriptionService;

    public SubscriptionController(ISubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDTO>> getAll() {
        return ResponseEntity.ok(subscriptionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponseDTO> create(@Valid @RequestBody SubscriptionRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionService.create(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody SubscriptionRequestDTO requestDTO) {
        return ResponseEntity.ok(subscriptionService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subscriptionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
