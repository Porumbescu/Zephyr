package com.porumb.zephyr.request;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String email;
    private String password;
    private String role;
}
