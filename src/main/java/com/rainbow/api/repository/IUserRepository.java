package com.rainbow.api.repository;

import com.rainbow.api.entity.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserSec, Long> {

    Optional<UserSec> findUserEntityByUsername(String username);

    Optional<UserSec> findByUsername(String username);
}
