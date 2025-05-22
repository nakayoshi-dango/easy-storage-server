/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.izanyfran.easy_storage.controller;

import com.izanyfran.easy_storage.entity.Collection;
import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.service.CollectionService;
import com.izanyfran.easy_storage.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nakayoshi_dango
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CollectionService collectionService;
    
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()).isEmpty()) {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Se ha creado el usuario " + createdUser.getUsername() + " con ID " + createdUser.getId() + ".");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ya existe un usuario con ese nombre.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        Optional<User> optOldUser = userService.getUserById(user.getId());
        if (optOldUser.isPresent()) {
            user.setCreatedAt(optOldUser.get().getCreatedAt());
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("Se ha actualizado el usuario " + updatedUser.getUsername() + " con ID " + updatedUser.getId() + ".");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No existe ningún producto con ese ID.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam String username) {
        Optional<User> optOldUser = userService.getUserByUsername(username);
        if (optOldUser.isPresent()) {
            User user = optOldUser.get();
            userService.deleteUserByUsername(username);
            return ResponseEntity.status(HttpStatus.OK).body("Se ha eliminado el usuario " + user.getUsername() + " con ID " + user.getId() + ".");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No existe ningún usuario con ese ID.");
        }
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/getAllCollections")
    public ResponseEntity<?> collectionsAll() {
        List<Collection> allCollections = collectionService.getAllCollections();
        if (allCollections.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aún no existen colecciones.");
        } else {
            return ResponseEntity.ok(collectionService.toDTOList(allCollections));
        }
    }
}
