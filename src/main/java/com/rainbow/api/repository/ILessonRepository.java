package com.rainbow.api.repository;

import com.rainbow.api.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILessonRepository extends JpaRepository<Lesson, Long> {
}
