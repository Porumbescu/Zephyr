package com.porumb.zephyr.controller;

import com.porumb.zephyr.config.JwtUtil;
import com.porumb.zephyr.request.LoginRequest;
import com.porumb.zephyr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @PostMapping("token")
    public ResponseEntity<String> getToken(@RequestBody LoginRequest loginRequest) {
        String token = JwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(token);
    }
}
