package com.porumb.zephyr.Exception;

public class QuestionSetNotFoundException extends RuntimeException {
    public QuestionSetNotFoundException(Long userId) {
        super("QuestionSet with ID " + userId + " not found.");
    }
}

