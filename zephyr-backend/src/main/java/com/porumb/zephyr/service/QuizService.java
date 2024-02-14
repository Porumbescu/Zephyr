package com.porumb.zephyr.service;

import com.porumb.zephyr.dao.QuestionDao;
import com.porumb.zephyr.dao.QuizDao;
import com.porumb.zephyr.dao.UserDao;
import com.porumb.zephyr.dao.UserQuizHistoryDao;
import com.porumb.zephyr.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;
    @Autowired
    private UserQuizHistoryDao userQuizHistoryDao;
    @Autowired
    private UserDao userDao;

    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        try {
            return new ResponseEntity<>(quizDao.findAll(), HttpStatus.OK);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> createQuiz(String category, String difficulty, int numQ, String title){

        List<Question> questions = questionDao.findQuestions(category, difficulty, numQ);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setCategory(category);
        quiz.setQuestions(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Quiz created successfully.", HttpStatus.CREATED);
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

//    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
//        Optional<Quiz> quizOpt = quizDao.findById(id);
//        if (quizOpt.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        Quiz quiz = quizOpt.get();
//        List<Question> questions = quiz.getQuestions();
//        int rightAnswer = 0;
//        for(Response r : responses) {
//            Question matchingQuestion = questions.stream()
//                    .filter(q -> Objects.equals(q.getId(), r.getId()))
//                    .findFirst()
//                    .orElse(null);
//
//            if (matchingQuestion != null && r.getAnswer().equals(matchingQuestion.getRightAnswer())) {
//                rightAnswer++;
//            }
//        }
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            String userEmail = (String) authentication.getPrincipal();
//            User currentUser = userDao.findByEmail(userEmail);
//            if (currentUser != null) {
//                UserQuizHistory historyEntry = new UserQuizHistory(currentUser, quiz);
//                userQuizHistoryDao.save(historyEntry);
//            }
//        }
//
//        return new ResponseEntity<>(rightAnswer, HttpStatus.OK);
//    }


    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questions = quiz.get().getQuestions();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
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

        User user = userDao.findByEmail(userEmail);
        if (user != null) {
            updateStreak(user);
            userDao.save(user);
        }

        return new ResponseEntity<>(rightAnswer, HttpStatus.OK);
    }

    private void updateStreak(User user) {
        Date now = new Date();
        LocalDate lastQuizLocalDate = null;
        if (user.getLastQuizDate() != null) {
            lastQuizLocalDate = user.getLastQuizDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        LocalDate nowLocalDate = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (lastQuizLocalDate == null || !lastQuizLocalDate.isEqual(nowLocalDate)) {
            user.setStreak(user.getStreak() + 1);
        }
        user.setLastQuizDate(now);
    }

    public ResponseEntity<String> deleteQuiz(Integer id) {
        if (!quizDao.existsById(id)) {
            return new ResponseEntity<>("Quiz not found.", HttpStatus.NOT_FOUND);
        }
        quizDao.deleteById(id);
        return new ResponseEntity<>("Quiz deleted successfully.", HttpStatus.OK);
    }


}
