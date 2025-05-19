package com.izanyfran.easy_storage.controller;

import com.izanyfran.easy_storage.security.JwtUtil;
import com.izanyfran.easy_storage.service.UserService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> register(@RequestParam String username, @RequestParam String password) {
        if (userService.registerUser(username, password)) {
            return ResponseEntity.ok("Usuario registrado");
        } else {
            return ResponseEntity.badRequest().body("ERROR: El nombre especificado ya está ocupado");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        boolean authenticated = userService.authenticate(username, password);
        if (authenticated) {
            String token = jwtUtil.generateToken(username, userService.getUserRole(username));
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    } 
    
    @GetMapping("/token")
    public ResponseEntity<String> isMyTokenValid(@RequestParam String token) {
        boolean isTokenValid = jwtUtil.validateToken(token);
        if (isTokenValid) {
            return ResponseEntity.status(HttpStatus.OK).body("Token válido");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Token inválido ");
        }
    } 
}

