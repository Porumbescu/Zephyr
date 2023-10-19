package com.porumb.zephyr.Exception;

public class QuestionNotFoundException extends RuntimeException{
    public QuestionNotFoundException(Long userId) {
        super("Question with ID " + userId + " not found.");
    }
}
