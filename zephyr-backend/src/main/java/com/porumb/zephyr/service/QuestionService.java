package com.porumb.zephyr.service;

import com.porumb.zephyr.dao.QuestionDao;
import com.porumb.zephyr.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;
    public List<Question> getALlQuestions() {
        return questionDao.findAll();
    }
}
