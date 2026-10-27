package com.rainbow.api.service;

import com.rainbow.api.entity.UserSec;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    public List<UserSec>findAll();
    public Optional<UserSec> findById(Long id);
    public UserSec save(UserSec userSec);
    public void deleteById(Long id);
    public UserSec update(UserSec userSec);
    public String encryptPassword(String password);
}
