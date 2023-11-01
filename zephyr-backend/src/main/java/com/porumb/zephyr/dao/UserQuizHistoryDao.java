package com.porumb.zephyr.dao;

import com.porumb.zephyr.model.Quiz;
import com.porumb.zephyr.model.UserQuizHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserQuizHistoryDao extends JpaRepository<UserQuizHistory, Integer> {

    @Query("SELECT uqh.quiz FROM UserQuizHistory uqh WHERE uqh.user.id = ?1")
    List<Quiz> findCompletedQuizzesByUserId(Integer userId);
}
