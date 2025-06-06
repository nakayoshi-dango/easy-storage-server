package com.izanyfran.easy_storage.controller;

import com.izanyfran.easy_storage.entity.Collection;
import com.izanyfran.easy_storage.entity.ProductCollection;
import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.service.CollectionService;
import com.izanyfran.easy_storage.service.ProductCollectionService;
import com.izanyfran.easy_storage.service.UserCollectionService;
import com.izanyfran.easy_storage.service.UserService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collections")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private ProductCollectionService productCollectionService;

    @Autowired
    private UserCollectionService userCollectionService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/mine")
    public ResponseEntity<?> collectionsMine() { //Colecciones donde el usuario es dueño
        // Obtener el nombre del usuario autenticado directamente desde el SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<Collection> userCollections = collectionService.getUserCollections(username);
        if (userCollections.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existen colecciones creadas por el usuario.");
        } else {
            userCollections.sort(Comparator.comparing(Collection::getName));
            return ResponseEntity.ok(collectionService.toDTOList(userCollections));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/visible")
    public ResponseEntity<?> collectionsVisible() { //Colecciones donde el usuario es miembro o es dueño
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<Collection> memberCollections = userCollectionService.getCollectionsByUserName(username);
        List<Collection> ownerCollections = collectionService.getUserCollections(username);
        Set<Collection> visibleCollectionsSet = new HashSet<>(ownerCollections);
        visibleCollectionsSet.addAll(memberCollections);
        List<Collection> visibleCollectionsList = new ArrayList<>(visibleCollectionsSet);
        if (visibleCollectionsList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existen colecciones visibles para el usuario.");
        } else {
            visibleCollectionsList.sort(Comparator.comparing(Collection::getName));
            return ResponseEntity.ok(collectionService.toDTOList(visibleCollectionsList));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{collectionName}/products")
    public ResponseEntity<?> productsInCollection(@PathVariable String collectionName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userService.getUserByUsername(username).get();
        Optional<Collection> col = collectionService.getCollectionByName(collectionName);
        if (col.isPresent()) {
            if (hasAccess(col.get(), user)) {
                List<ProductCollection> productsCollection = productCollectionService.getRelationsByCollectionName(collectionName);
                if (productsCollection.isEmpty()) {
                    return ResponseEntity.ok("No existen productos en esta colección.");
                } else {
                    productsCollection.sort((p1, p2) -> {
                        // lógica personalizada
                        return p1.getProduct().getName().compareToIgnoreCase(p2.getProduct().getName());
                    });
                    return ResponseEntity.ok(productCollectionService.toDTOList(productsCollection));
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes acceso a esta colección.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esta colección no existe.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{collectionName}/users")
    public ResponseEntity<?> usersInCollection(@PathVariable String collectionName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userService.getUserByUsername(username).get();
        Optional<Collection> col = collectionService.getCollectionByName(collectionName);
        if (col.isPresent()) {
            if (hasAccess(col.get(), user)) {
                List<User> members = userCollectionService.getUsersByCollectionName(collectionName);
                if (members.isEmpty()) {
                    return ResponseEntity.ok("No existen miembros en esta colección.");
                } else {
                    members.sort(Comparator.comparing(User::getUsername));
                    return ResponseEntity.ok(userService.toDTOList(members));
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes acceso a esta colección.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esta colección no existe.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{collectionName}/nonusers")
    public ResponseEntity<?> usersNotInCollection(@PathVariable String collectionName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = userService.getUserByUsername(username).get();
        Optional<Collection> col = collectionService.getCollectionByName(collectionName);
        if (col.isPresent()) {
            if (hasAccess(col.get(), user)) {
                List<User> nonmembers = userCollectionService.getUsersNotInCollection(collectionName);
                nonmembers.sort(Comparator.comparing(User::getUsername));
                return ResponseEntity.ok(userService.toDTOList(nonmembers));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes acceso a esta colección.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Esta colección no existe.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/getCollection")
    public ResponseEntity<?> getCollection(@RequestParam String collectionName) {
        Optional<Collection> optCollection = collectionService.getCollectionByName(collectionName);
        if (optCollection.isPresent()) {
            Collection collection = optCollection.get();
            return ResponseEntity.ok(collectionService.toDTO(collection));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No existe ninguna colección con ese nombre.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/createCollection")
    public ResponseEntity<String> createCollection(@RequestBody Collection collection) {
        if (collectionService.getCollectionByName(collection.getName()).isEmpty()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) authentication.getPrincipal();
            User owner = userService.getUserByUsername(username).get();
            collection.setOwner(owner);
            Collection createdCollection = collectionService.createCollection(collection);
            return ResponseEntity.status(HttpStatus.CREATED).body("Se ha creado la colección " + createdCollection.getName() + " con ID " + createdCollection.getId() + ".");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ya existe una colección con ese nombre.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/updateCollection")
    public ResponseEntity<String> updateCollection(@RequestBody Collection collection) {
        Optional<Collection> optOldCollection = collectionService.getCollectionById(collection.getId());
        if (optOldCollection.isPresent()) {
            Collection oldCollection = optOldCollection.get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) authentication.getPrincipal();
            User user = userService.getUserByUsername(username).get();
            if (hasSudoAccess(oldCollection, user)) {
                collection.setOwner(oldCollection.getOwner());
                collection.setCreationDate(oldCollection.getCreationDate());
                Collection updatedCollection = collectionService.updateCollection(collection);
                return ResponseEntity.status(HttpStatus.OK).body("Se ha actualizado la colección " + updatedCollection.getName() + " con ID " + updatedCollection.getId() + ".");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos superiores sobre esa colección");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No existe ningún producto con ese ID.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/deleteCollection")
    public ResponseEntity<String> deleteCollection(@RequestParam String collectionName) {
        Optional<Collection> optCollection = collectionService.getCollectionByName(collectionName);
        if (optCollection.isPresent()) {
            Collection collection = optCollection.get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) authentication.getPrincipal();
            User user = userService.getUserByUsername(username).get();
            if (hasSudoAccess(collection, user)) {
                collectionService.deleteCollectionByName(collectionName);
                return ResponseEntity.status(HttpStatus.OK).body("Se ha eliminado la colección " + collection.getName() + " con ID " + collection.getId() + ".");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos superiores sobre ese producto");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No existe un producto con ese ID.");
        }
    }

    private Boolean hasAccess(Collection collection, User user) {
        List<User> members = userCollectionService.getUsersByCollectionName(collection.getName());
        return collection.getOwner().getUsername().equals(user.getUsername())
                || user.getRole().equals("ROLE_ADMIN") || members.contains(user);
    }

    private Boolean hasSudoAccess(Collection collection, User user) {
        return collection.getOwner().getUsername().equals(user.getUsername())
                || user.getRole().equals("ROLE_ADMIN");
    }

}
