package com.izanyfran.easy_storage.controller;

import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.security.JwtUtil;
import com.izanyfran.easy_storage.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userService.registerUser(user.getUsername(), user.getPassword())) {
            return ResponseEntity.ok("Usuario registrado");
        } else {
            return ResponseEntity.badRequest().body("ERROR: El nombre especificado ya está ocupado");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        boolean authenticated = userService.authenticate(user.getUsername(), user.getPassword());
        if (authenticated) {
            String token = jwtUtil.generateToken(user.getUsername(), userService.getUserRole(user.getUsername()));
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    } 
}

