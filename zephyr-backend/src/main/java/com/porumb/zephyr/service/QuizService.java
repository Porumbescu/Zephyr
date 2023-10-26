package com.porumb.zephyr.service;

import com.porumb.zephyr.dao.QuestionDao;
import com.porumb.zephyr.dao.QuizDao;
import com.porumb.zephyr.model.Question;
import com.porumb.zephyr.model.QuestionWrapper;
import com.porumb.zephyr.model.Quiz;
import com.porumb.zephyr.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, String difficulty, int numQ, String title){

        List<Question> questions = questionDao.findQuestions(category, difficulty, numQ);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {

        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questionsFromDB = quiz.get().getQuestions();
        List<QuestionWrapper> questionsForUser = new ArrayList<>();

        for(Question q : questionsFromDB){
            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
            questionsForUser.add(qw);
        }

        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questions = quiz.get().getQuestions();
        int rightAnswer = 0;
        for(Response r : responses) {
            Question matchingQuestion = questions.stream()
                    .filter(q -> Objects.equals(q.getId(), r.getId()))
                    .findFirst()
                    .orElse(null);

            if (matchingQuestion != null && r.getAnswer().equals(matchingQuestion.getRightAnswer())) {
                rightAnswer++;
            }
        }
        return new ResponseEntity<>(rightAnswer, HttpStatus.OK);
    }
}
