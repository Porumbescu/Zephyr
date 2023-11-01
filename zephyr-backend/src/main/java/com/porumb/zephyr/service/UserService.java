package com.porumb.zephyr.service;

import com.porumb.zephyr.config.JwtUtil;
import com.porumb.zephyr.dao.UserDao;
import com.porumb.zephyr.dao.UserQuizHistoryDao;
import com.porumb.zephyr.model.Quiz;
import com.porumb.zephyr.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
