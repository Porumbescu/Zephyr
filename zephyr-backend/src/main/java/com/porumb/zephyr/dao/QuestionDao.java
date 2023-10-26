package com.porumb.zephyr.dao;

import com.porumb.zephyr.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {
    List<Question> findByCategory(String category);

    @Query(value = "SELECT * FROM question q Where q.category=:category AND q.difficulty_level=:difficulty ORDER BY RANDOM() LIMIT :numQ", nativeQuery = true)
    List<Question> findQuestions(String category, String difficulty, int numQ);
}
