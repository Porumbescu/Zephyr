package com.porumb.zephyr.Service;

import com.porumb.zephyr.Entity.QuestionSet;
import com.porumb.zephyr.Exception.QuestionSetNotFoundException;
import com.porumb.zephyr.Repository.QuestionSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionSetService {

    @Autowired
    private QuestionSetRepository questionSetRepository;

    public QuestionSet createQuestionSet(QuestionSet questionSet) {
        return questionSetRepository.save(questionSet);
    }

    public Optional<QuestionSet> findQuestionSetById(Long id) {
        return questionSetRepository.findById(id);
    }


    public QuestionSet updateQuestionSet(Long id, QuestionSet updatedQuestionSet) {
        Optional<QuestionSet> existingQuestionSetOpt = questionSetRepository.findById(id);

        if (existingQuestionSetOpt.isEmpty()) {
            throw new QuestionSetNotFoundException(id);
        }

        QuestionSet existingQuestionSet = existingQuestionSetOpt.get();

        existingQuestionSet.getQuestions().clear();
        existingQuestionSet.getQuestions().addAll(updatedQuestionSet.getQuestions());
        existingQuestionSet.setName(updatedQuestionSet.getName());

        return questionSetRepository.save(existingQuestionSet);
    }

    public void deleteQuestionSet(Long id) {
        if (!questionSetRepository.existsById(id)) {
            throw new QuestionSetNotFoundException(id);
        }
        questionSetRepository.deleteById(id);
    }
}

