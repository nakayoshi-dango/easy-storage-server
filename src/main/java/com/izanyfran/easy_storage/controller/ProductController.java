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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCollectionService productCollectionService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<?> productsAll() {
        List<Product> allProducts = productService.getAllProducts();
        if (allProducts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aún no existen productos.");
        } else {
            return ResponseEntity.ok(productService.toDTOList(allProducts));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/mine")
    public ResponseEntity<?> productsMine() {
        // Obtener el nombre del usuario autenticado directamente desde el SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<Product> userProducts = productService.getUserProducts(username);
        if (userProducts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existen productos subidos por el usuario.");
        } else {
            return ResponseEntity.ok(productService.toDTOList(userProducts));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/addto")
    public ResponseEntity<?> addProductToCollection(@RequestParam String productId, @RequestParam String collectionName, @RequestParam int quantity) {
        // Obtener el nombre del usuario autenticado directamente desde el SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userService.getUserByUsername(username).get();
        Optional<Collection> col = collectionService.getCollectionByName(collectionName);
        if (col.isPresent()) {
            //Si el usuario es admin o es el propietario
            if (user.getRole().equals("ROLE_ADMIN") || username.equals(col.get().getOwner().getUsername())) {
                String productName = productService.getProductById(productId).get().getName();
                if (productCollectionService.isProductInCollection(productId, collectionName)) {
                    ProductCollection pc = productCollectionService.getRelation(collectionName, productId).get();
                    pc.setQuantity(pc.getQuantity() + quantity);
                    pc = productCollectionService.updateRelation(pc);
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body("Se ha añadido "
                            + quantity + " " + productName + " más a la colección " + collectionName
                            + ".\n Ahora hay un total de " + pc.getQuantity() + ".");
                } else {
                    try {
                        productCollectionService.addProductToCollection(productId, collectionName, quantity);
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.CREATED).body("Error al añadir producto a colección.");
                    }
                    return ResponseEntity.status(HttpStatus.CREATED).body("Se ha añadido " + quantity + " " + productName + " a la colección " + collectionName + ".");
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes acceso a esta colección.");
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esta colección no existe.");
        }
    }

}
