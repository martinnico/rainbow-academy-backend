package com.rainbow.api.service;

import com.rainbow.api.entity.UserSec;
import com.rainbow.api.repository.IUserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final IUserRepository iUserRepository;
    public UserService(IUserRepository iUserRepository){
        this.iUserRepository = iUserRepository;
    }

    @Override
    public List<UserSec> findAll() {
        return iUserRepository.findAll();
    }

    @Override
    public Optional<UserSec> findById(Long id) {
        return iUserRepository.findById(id);
    }

    @Override
    public UserSec save(UserSec userSec) {
        return iUserRepository.save(userSec);
    }

    @Override
    public void deleteById(Long id) {
        iUserRepository.deleteById(id);

    }

    @Override
    public UserSec update(UserSec userSec) {
        return iUserRepository.save(userSec);
    }

    @Override
    public String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
