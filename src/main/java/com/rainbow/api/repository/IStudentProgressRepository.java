package com.rainbow.api.repository;

import com.rainbow.api.entity.StudentProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStudentProgressRepository extends JpaRepository<StudentProgress, Long> {
}
