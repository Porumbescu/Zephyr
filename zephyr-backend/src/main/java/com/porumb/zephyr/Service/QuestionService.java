package com.porumb.zephyr.Service;

import com.porumb.zephyr.Entity.Question;
import com.porumb.zephyr.Exception.QuestionNotFoundException;
import com.porumb.zephyr.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Optional<Question> findQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public Question updateQuestion(Long id, Question updatedQuestion) {
        Optional<Question> existingQuestionOpt = questionRepository.findById(id);

        if (existingQuestionOpt.isEmpty()) {
            throw new QuestionNotFoundException(id);
        }

        Question existingQuestion = existingQuestionOpt.get();

        existingQuestion.setCategory(updatedQuestion.getCategory());
        existingQuestion.setOption1(updatedQuestion.getOption1());
        existingQuestion.setOption2(updatedQuestion.getOption2());
        existingQuestion.setOption3(updatedQuestion.getOption3());
        existingQuestion.setOption4(updatedQuestion.getOption4());
        existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());

        return questionRepository.save(existingQuestion);
    }

    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new QuestionNotFoundException(id);
        }

        questionRepository.deleteById(id);
    }
}
