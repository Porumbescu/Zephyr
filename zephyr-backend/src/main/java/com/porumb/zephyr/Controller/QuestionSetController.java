package com.porumb.zephyr.Controller;

import com.porumb.zephyr.Entity.QuestionSet;
import com.porumb.zephyr.Exception.QuestionSetNotFoundException;
import com.porumb.zephyr.Service.QuestionSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question-sets")
public class QuestionSetController {

    @Autowired
    private QuestionSetService questionSetService;

    @PostMapping
    public ResponseEntity<QuestionSet> createQuestionSet(@RequestBody QuestionSet questionSet) {
        QuestionSet createdQuestionSet = questionSetService.createQuestionSet(questionSet);
        return new ResponseEntity<>(createdQuestionSet, HttpStatus.CREATED);
    }

    //...

    @GetMapping("/{id}")
    public ResponseEntity<QuestionSet> getQuestionSetById(@PathVariable Long id) {
        return questionSetService.findQuestionSetById(id)
                .map(questionSet -> new ResponseEntity<>(questionSet, HttpStatus.OK))
                .orElseThrow(() -> new QuestionSetNotFoundException(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionSet> updateQuestionSet(@PathVariable Long id, @RequestBody QuestionSet updatedQuestionSet) {
        QuestionSet questionSet = questionSetService.updateQuestionSet(id, updatedQuestionSet);
        return new ResponseEntity<>(questionSet, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestionSet(@PathVariable Long id) {
        questionSetService.deleteQuestionSet(id);
        return ResponseEntity.noContent().build();
    }

}

