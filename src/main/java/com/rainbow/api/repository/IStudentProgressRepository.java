package com.rainbow.api.repository;

import com.rainbow.api.entity.StudentProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IStudentProgressRepository extends JpaRepository<StudentProgress, Long> {
    Optional<StudentProgress> findByStudentIdAndLessonId(Long studentId, Long lessonId);
    List<StudentProgress> findByStudentIdAndLesson_CourseIdOrderByLesson_OrderIndexAsc(Long studentId, Long courseId);
}
