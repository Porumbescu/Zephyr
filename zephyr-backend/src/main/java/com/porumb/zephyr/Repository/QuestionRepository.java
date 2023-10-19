package com.porumb.zephyr.Repository;

import com.porumb.zephyr.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByCategory(String category);
}

