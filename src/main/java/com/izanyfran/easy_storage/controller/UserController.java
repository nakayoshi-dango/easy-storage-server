package com.izanyfran.easy_storage.controller;

import com.izanyfran.easy_storage.entity.Collection;
import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.service.CollectionService;
import com.izanyfran.easy_storage.service.UserCollectionService;
import com.izanyfran.easy_storage.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private UserCollectionService userCollectionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        List<User> allusers = userService.getAllUsers();
        if (allusers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aún no existen usuarios.");
        } else {
            return ResponseEntity.ok(userService.toDTOList(allusers));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/myInfo")
    public ResponseEntity<?> myInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        Optional<User> optUser = userService.getUserByUsername(username);
        if (optUser.isPresent()) {
            User user = optUser.get();
            return ResponseEntity.ok(userService.toDTO(user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe ningún usuario con ese nombre.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestParam String username) {
        Optional<User> optUser = userService.getUserByUsername(username);
        if (optUser.isPresent()) {
            User user = optUser.get();
            return ResponseEntity.ok(userService.toDTO(user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe ningún usuario con ese nombre.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/setPic")
    public ResponseEntity<?> setPic(@RequestParam String imgURL) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        Optional<User> optUser = userService.getUserByUsername(username);
        User user = optUser.get();
        user.setImageURL(imgURL);
        userService.updateUser(user);
        return ResponseEntity.ok().body("Foto de perfil cambiada");
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/addToCollection")
    public ResponseEntity<?> addUserToCollection(@RequestParam String collectionName, @RequestParam String usernameToAdd) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userService.getUserByUsername(username).get();
        Optional<Collection> col = collectionService.getCollectionByName(collectionName);
        if (col.isPresent()) {
            Optional<User> userToAdd = userService.getUserByUsername(usernameToAdd);
            if (userToAdd.isPresent()) {
                if (hasSudoAccess(col.get(), user)) {
                    if (userCollectionService.addUserToCollection(userToAdd.get().getId(), col.get().getId())) {
                        return ResponseEntity.status(HttpStatus.OK).body(usernameToAdd + " es ahora miembro de la colección " + col.get().getName() + ".");
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El usuario especificado ya era miembro de la colección.");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos superiores sobre esta colección.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario especificado no existe.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esta colección no existe.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/deleteFromCollection")
    public ResponseEntity<?> deleteUserFromCollection(@RequestParam String collectionName, @RequestParam String usernameToDelete) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userService.getUserByUsername(username).get();
        Optional<Collection> col = collectionService.getCollectionByName(collectionName);
        if (col.isPresent()) {
            Optional<User> userToDelete = userService.getUserByUsername(usernameToDelete);
            if (userToDelete.isPresent()) {
                if (hasSudoAccess(col.get(), user)) {
                    if (userCollectionService.removeUserFromCollection(userToDelete.get().getId(), col.get().getId())) {
                        return ResponseEntity.status(HttpStatus.OK).body(usernameToDelete + " ha sido expulsado de la colección " + col.get().getName() + ".");
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("El usuario especificado no era miembro de la colección.");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos superiores sobre esta colección.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario especificado no existe.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esta colección no existe.");
        }
    }

    private Boolean hasSudoAccess(Collection collection, User user) {
        return collection.getOwner().getUsername().equals(user.getUsername())
                || user.getRole().equals("ROLE_ADMIN");
    }

}
