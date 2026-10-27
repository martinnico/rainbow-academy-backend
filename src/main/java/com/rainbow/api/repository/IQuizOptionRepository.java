package com.rainbow.api.repository;

import com.rainbow.api.entity.QuizOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuizOptionRepository extends JpaRepository<QuizOption, Long> {
}
