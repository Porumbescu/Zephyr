package com.porumb.zephyr.dao;

import com.porumb.zephyr.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizDao extends JpaRepository<Quiz, Integer> {
}
