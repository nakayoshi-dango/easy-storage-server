package com.izanyfran.easy_storage.controller;

import com.izanyfran.easy_storage.entity.Collection;
import com.izanyfran.easy_storage.entity.Product;
import com.izanyfran.easy_storage.entity.ProductCollection;
import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.service.CollectionService;
import com.izanyfran.easy_storage.service.ProductCollectionService;
import com.izanyfran.easy_storage.service.ProductService;
import com.izanyfran.easy_storage.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collections")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private ProductCollectionService productCollectionService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<?> collectionsAll() {
        List<Collection> allCollections = collectionService.getAllCollections();
        if (allCollections.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aún no existen colecciones.");
        } else {
            return ResponseEntity.ok(collectionService.toDTOList(allCollections));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/mine")
    public ResponseEntity<?> collectionsMine() {
        // Obtener el nombre del usuario autenticado directamente desde el SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<Collection> userCollections = collectionService.getUserCollections(username);
        if (userCollections.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existen colecciones creadas por el usuario.");
        } else {
            return ResponseEntity.ok(collectionService.toDTOList(userCollections));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{collectionName}/products")
    public ResponseEntity<?> productsInCollection(@PathVariable String collectionName) {
        // Obtener el nombre del usuario autenticado directamente desde el SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userService.getUserByUsername(username).get();
        Optional<Collection> col = collectionService.getCollectionByName(collectionName);
        if (col.isPresent()) {
            //Si el usuario es admin o es el propietario
            if (user.getRole().equals("ROLE_ADMIN") || username.equals(col.get().getOwner().getUsername())) {
                List<ProductCollection> productsCollection = productCollectionService.getRelationsByCollectionName(collectionName);
                if (productsCollection.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existen productos en esta colección.");
                } else {
                    return ResponseEntity.ok(productCollectionService.toDTOList(productsCollection));
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes acceso a esta colección.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esta colección no existe.");
        }
    }

}
