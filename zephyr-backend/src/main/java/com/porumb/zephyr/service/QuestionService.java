package com.porumb.zephyr.service;

import com.porumb.zephyr.dao.QuestionDao;
import com.porumb.zephyr.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;
    public ResponseEntity<List<Question>> getALlQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Question>> getQuestionByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionDao.save(question);
        }catch(Exception e){
            return new ResponseEntity<>("Question couldn't be created.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Question added.", HttpStatus.CREATED);
    }

    public ResponseEntity<String> editQuestion(Integer questionId, Question newQuestion){
        if (!questionDao.existsById(questionId)) {
            return new ResponseEntity<>("Question not found.", HttpStatus.NOT_FOUND);
        }
        newQuestion.setId(questionId);
        questionDao.save(newQuestion);
        return new ResponseEntity<>("Question edited.", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteQuestion(Integer questionId){
        if(!questionDao.existsById(questionId)){
            return new ResponseEntity<>("Question not found.", HttpStatus.NOT_FOUND);
        }
        questionDao.deleteById(questionId);
        return new ResponseEntity<>("Question " + questionId + " deleted.", HttpStatus.OK);
    }
}
