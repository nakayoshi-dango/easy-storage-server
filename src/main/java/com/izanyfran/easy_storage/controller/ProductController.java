package com.izanyfran.easy_storage.controller;

import com.izanyfran.easy_storage.entity.Collection;
import com.izanyfran.easy_storage.entity.Product;
import com.izanyfran.easy_storage.entity.ProductCollection;
import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.service.CollectionService;
import com.izanyfran.easy_storage.service.ProductCollectionService;
import com.izanyfran.easy_storage.service.ProductService;
import com.izanyfran.easy_storage.service.UserCollectionService;
import com.izanyfran.easy_storage.service.UserService;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Autowired
    private UserCollectionService userCollectionService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getAllProducts")
    public ResponseEntity<?> productsAll() {
        List<Product> allProducts = productService.getAllProducts();
        if (allProducts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aún no existen productos.");
        } else {
            allProducts.sort(Comparator.comparing(Product::getName));
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
            userProducts.sort(Comparator.comparing(Product::getName));
            return ResponseEntity.ok(productService.toDTOList(userProducts));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/mineCount")
    public ResponseEntity<?> productsMineCount() {
        // Obtener el nombre del usuario autenticado directamente desde el SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<Product> userProducts = productService.getUserProducts(username);
        if (userProducts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existen productos subidos por el usuario.");
        } else {
            return ResponseEntity.ok(userProducts.size());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/addToCollection")
    public ResponseEntity<?> addProductToCollection(@RequestParam String productId, @RequestParam String collectionName, @RequestParam int quantity) {
        // Obtener el nombre del usuario autenticado directamente desde el SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userService.getUserByUsername(username).get();
        Optional<Collection> col = collectionService.getCollectionByName(collectionName);
        Optional<Product> product = productService.getProductById(productId);
        if (col.isPresent()) {
            if (product.isPresent()) {
                if (hasAccess(col.get(), user)) {
                    String productName = product.get().getName();
                    if (productCollectionService.isProductInCollection(productId, collectionName)) {
                        ProductCollection pc = productCollectionService.getRelation(collectionName, productId).get();
                        if ((pc.getQuantity() + quantity) < 0) {
                            pc.setQuantity(0);
                        } else {
                            pc.setQuantity(pc.getQuantity() + quantity);
                        }
                        pc = productCollectionService.updateRelation(pc);
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Se ha añadido "
                                + quantity + " " + productName + " más a la colección " + collectionName
                                + ".\n Ahora hay un total de " + pc.getQuantity() + ".");
                    } else {
                        try {
                            productCollectionService.addProductToCollection(productId, collectionName, quantity);
                        } catch (Exception e) {
                            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Error al añadir producto a colección.");
                        }
                        return ResponseEntity.status(HttpStatus.CREATED).body("Se ha añadido " + quantity + " " + productName + " a la colección " + collectionName + ".");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes acceso a esta colección.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Este producto no existe.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esta colección no existe.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/deleteFromCollection")
    public ResponseEntity<?> deleteProductFromCollection(@RequestParam String productId, @RequestParam String collectionName) {
        // Obtener el nombre del usuario autenticado directamente desde el SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userService.getUserByUsername(username).get();
        Optional<Collection> col = collectionService.getCollectionByName(collectionName);
        Optional<Product> product = productService.getProductById(productId);
        if (col.isPresent()) {
            if (product.isPresent()) {
                if (hasAccess(col.get(), user)) {
                    if (productCollectionService.isProductInCollection(productId, collectionName)) {
                        ProductCollection pc = productCollectionService.getRelation(collectionName, productId).get();
                        productCollectionService.deleteRelation(pc);
                        return ResponseEntity.status(HttpStatus.OK).body(
                                "Se ha eliminado " + product.get().getName() + " de la colección "
                                + collectionName + ".");
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no estaba en la colección.");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes acceso a esta colección.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Este producto no existe.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esta colección no existe.");
        }
    }

    private Boolean hasAccess(Collection collection, User user) {
        List<User> members = userCollectionService.getUsersByCollectionName(collection.getName());
        return collection.getOwner().getUsername().equals(user.getUsername())
                || user.getRole().equals("ROLE_ADMIN") || members.contains(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getProduct")
    public ResponseEntity<?> getProduct(@RequestParam String productId) {
        Optional<Product> optProduct = productService.getProductById(productId);
        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            return ResponseEntity.ok(productService.toDTO(product));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No existe ningún producto con ese ID.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/createProduct")
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        if (productService.getProductById(product.getId()).isEmpty()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) authentication.getPrincipal();
            User uploader = userService.getUserByUsername(username).get();
            product.setUploader(uploader);
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body("Se ha creado el producto " + createdProduct.getName() + " con ID " + createdProduct.getId() + ".");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ya existe un producto con ese ID.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/updateProduct")
    public ResponseEntity<String> updateProduct(@RequestBody Product product) {
        Optional<Product> optOldProduct = productService.getProductById(product.getId());
        if (optOldProduct.isPresent()) {
            Product oldProduct = optOldProduct.get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) authentication.getPrincipal();
            User user = userService.getUserByUsername(username).get();
            if (hasAccess(oldProduct, user)) {
                product.setUploader(oldProduct.getUploader());
                Product updatedProduct = productService.updateProduct(product);
                return ResponseEntity.status(HttpStatus.OK).body("Se ha actualizado el producto " + updatedProduct.getName() + " con ID " + updatedProduct.getId() + ".");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos sobre ese producto");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No existe ningún producto con ese ID.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/deleteProduct")
    public ResponseEntity<String> deleteProduct(@RequestParam String productId) {
        Optional<Product> optProduct = productService.getProductById(productId);
        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) authentication.getPrincipal();
            User user = userService.getUserByUsername(username).get();
            if (hasAccess(product, user)) {
                productService.deleteProduct(productId);
                return ResponseEntity.status(HttpStatus.OK).body("Se ha eliminado el producto con ID " + productId + ".");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos sobre ese producto");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No existe un producto con ese ID.");
        }
    }

    private Boolean hasAccess(Product product, User user) {
        return product.getUploader().getUsername().equals(user.getUsername())
                || user.getRole().equals("ROLE_ADMIN");
    }

}
