package com.izanyfran.easy_storage.controller;

import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/dashboard")
    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok("Bienvenido al panel de administración");
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/getall")
    public ResponseEntity<?> getAllUsers() {
        List<User> allusers = userService.getAllUsers();
        if (allusers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aún no existen usuarios.");
        } else {
            return ResponseEntity.ok(userService.toDTOList(allusers));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Bienvenido al panel de usuario");
    }
    
}
