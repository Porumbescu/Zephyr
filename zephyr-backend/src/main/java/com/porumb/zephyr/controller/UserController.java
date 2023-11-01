package com.porumb.zephyr.controller;

import com.porumb.zephyr.model.Quiz;
import com.porumb.zephyr.request.RegisterUserRequest;
import com.porumb.zephyr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest request) {
        logger.info("Entering registerUser with request: {}", request);
        ResponseEntity<String> response = userService.registerUser(request.getEmail(), request.getPassword(), request.getRole());
        logger.info("Exiting registerUser with response: {}", response);
        return response;
    }

    @GetMapping("{id}/completed-quizzes")
    public ResponseEntity<List<Quiz>> getCompletedQuizzes(@PathVariable Integer id) {
        return userService.getCompletedQuizzes(id);
    }
}



