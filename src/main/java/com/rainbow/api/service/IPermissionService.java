package com.rainbow.api.service;

import com.rainbow.api.entity.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {
    List<Permission> findAll();
    Optional<Permission> findById(Long id);
    Permission save(Permission permission);
    Permission update(Permission permission);
    void deleteById(Long id);
}
