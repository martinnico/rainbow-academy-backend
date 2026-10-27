package com.rainbow.api.repository;

import com.rainbow.api.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    List<QuizQuestion> findByActivityIdOrderByOrderIndexAsc(Long activityId);
}
