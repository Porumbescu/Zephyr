package com.porumb.zephyr.controller;

import com.porumb.zephyr.model.Question;
import com.porumb.zephyr.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/questions")
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @GetMapping
    public ResponseEntity<List<Question>>  getAllQuestions(){
        return questionService.getALlQuestions();
    }
    @GetMapping("category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category){
        return questionService.getQuestionByCategory(category);
    }

    @PostMapping
    public ResponseEntity<String> addQuestion(@RequestBody Question question){
        return questionService.addQuestion(question);
    }

    @PutMapping("{questionId}")
    public ResponseEntity<String> editQuestion(@PathVariable Integer questionId, @RequestBody Question question){
        return questionService.editQuestion(questionId, question);
    }
    @DeleteMapping("{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Integer questionId){
        return questionService.deleteQuestion(questionId);
    }
}
