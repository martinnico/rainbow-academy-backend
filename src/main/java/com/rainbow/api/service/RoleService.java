package com.rainbow.api.service;

import com.rainbow.api.entity.Role;
import com.rainbow.api.repository.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService implements IRoleService {

    private final IRoleRepository iRoleRepository;
    public RoleService (IRoleRepository iRoleRepository){
        this.iRoleRepository = iRoleRepository;
    }

    @Override
    public List<Role> findAll() {
        return iRoleRepository.findAll() ;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return iRoleRepository.findById(id);
    }

    @Override
    public Role save(Role role) {
        return iRoleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        return iRoleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        iRoleRepository.deleteById(id);
    }
}
