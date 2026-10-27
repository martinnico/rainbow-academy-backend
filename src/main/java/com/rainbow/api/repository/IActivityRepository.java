package com.rainbow.api.repository;

import com.rainbow.api.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IActivityRepository extends JpaRepository<Activity, Long> {
    Optional<Activity> findByLessonId(Long lessonId);
}
