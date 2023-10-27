package com.porumb.zephyr.controller;

import com.porumb.zephyr.config.JwtUtil;
import com.porumb.zephyr.model.User;
import com.porumb.zephyr.request.LoginRequest;
import com.porumb.zephyr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("token")
    public ResponseEntity<String> getToken(@RequestBody LoginRequest loginRequest) {
        User user = userService.findUserByEmail(loginRequest.getUsername());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = JwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(token);
    }
}
