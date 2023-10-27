package com.porumb.zephyr.controller;

import com.porumb.zephyr.model.QuestionWrapper;
import com.porumb.zephyr.model.Response;
import com.porumb.zephyr.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/quiz")
public class QuizController {
    @Autowired
    QuizService quizService;
    @Secured("ROLE_ADMIN")
    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestParam String category, @RequestParam String difficulty, @RequestParam int numQ, @RequestParam String title){
        return quizService.createQuiz(category, difficulty, numQ, title);
    }

    @GetMapping("{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id){
        return quizService.getQuizQuestions(id);
    }

    @PostMapping("{id}/submit")
    public ResponseEntity<Integer> submitQuiz(@PathVariable Integer id, @RequestBody List<Response> responses){
        return quizService.calculateResult(id, responses);
    }
}
