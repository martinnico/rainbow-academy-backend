package com.rainbow.api.service;

import com.rainbow.api.entity.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    List<Role> findAll();
    Optional<Role> findById(Long id);
    Role save(Role role);
    Role update(Role role);
    void deleteById(Long id);

}
