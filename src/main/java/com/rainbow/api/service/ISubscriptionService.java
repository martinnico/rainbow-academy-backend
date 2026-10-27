package com.rainbow.api.service;

import com.rainbow.api.dto.SubscriptionRequestDTO;
import com.rainbow.api.dto.SubscriptionResponseDTO;

import java.util.List;

public interface ISubscriptionService {
    List<SubscriptionResponseDTO> findAll();
    SubscriptionResponseDTO findById(Long id);
    SubscriptionResponseDTO create(SubscriptionRequestDTO requestDTO);
    SubscriptionResponseDTO update(Long id, SubscriptionRequestDTO requestDTO);
    void deleteById(Long id);
}
