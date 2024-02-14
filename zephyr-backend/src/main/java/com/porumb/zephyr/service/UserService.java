package com.porumb.zephyr.service;

import com.porumb.zephyr.config.JwtUtil;
import com.porumb.zephyr.dao.UserDao;
import com.porumb.zephyr.dao.UserQuizHistoryDao;
import com.porumb.zephyr.model.Quiz;
import com.porumb.zephyr.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserQuizHistoryDao userQuizHistoryDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public ResponseEntity<String> registerUser(String email, String password, String role) {
        logger.info("Entering registerUser with email: {}, password: {}, role: {}", email, password, role);
        try {
            if (userDao.findByEmail(email) != null) {
                return new ResponseEntity<>("Username already exists.", HttpStatus.CONFLICT);
            }
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            userDao.save(user);
            logger.info("User registered successfully.");
            return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("An error occurred while registering the user.", e);
            return new ResponseEntity<>("An error occurred while registering the user.", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> loginUser(String email, String password){
        User user = userDao.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return new ResponseEntity<>("Invalid credentials.", HttpStatus.UNAUTHORIZED);
        }

        String token = JwtUtil.generateToken(email);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    public ResponseEntity<List<Quiz>> getCompletedQuizzes(Integer userId) {
        List<Quiz> completedQuizzes = userQuizHistoryDao.findCompletedQuizzesByUserId(userId);
        return new ResponseEntity<>(completedQuizzes, HttpStatus.OK);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetStreaksForInactivity() {
        List<User> users = userDao.findAll();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        users.forEach(user -> {
            LocalDate lastQuizDate = user.getLastQuizDate() != null
                    ? user.getLastQuizDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    : null;

            if (lastQuizDate == null || !lastQuizDate.isEqual(yesterday)) {
                user.setStreak(0);
                userDao.save(user);
            }
        });
    }

    private boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate1.isEqual(localDate2);
    }
}
