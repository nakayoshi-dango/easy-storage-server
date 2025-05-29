package com.izanyfran.easy_storage.service;

import com.izanyfran.easy_storage.dto.CollectionDTO;
import com.izanyfran.easy_storage.entity.Collection;
import com.izanyfran.easy_storage.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.izanyfran.easy_storage.repository.CollectionRepository;
import com.izanyfran.easy_storage.repository.UserRepository;
import java.util.stream.Collectors;

@Service
public class CollectionService {

    @Autowired
    private CollectionRepository repositoryCollection;

    @Autowired
    private UserRepository repositoryUser;

    @Transactional
    public Collection createCollection(Collection collection) {
        return repositoryCollection.save(collection);
    }

    public List<Collection> getAllCollections() {
        return repositoryCollection.findAll();
    }

    public List<Collection> getUserCollections(String username) {
        Optional<User> user = repositoryUser.findByUsername(username);
        if (user.isPresent()) {
            User owner = user.get();
            return repositoryCollection.findByOwner(owner);
        } else {
            return List.of();
        }
    }

    public CollectionDTO toDTO(Collection collection) {
        return new CollectionDTO(
                collection.getId(),
                collection.getName(),
                collection.getDescription(),
                collection.getOwner().getUsername(), // Extrae solo el nombre del owner
                collection.getCreationDate(),
                collection.getImageURL()
        );
    }

    public List<CollectionDTO> toDTOList(List<Collection> collections) {
        return collections.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<Collection> getCollectionById(Integer id) {
        return repositoryCollection.findById(id);
    }

    public Optional<Collection> getCollectionByName(String name) {
        return repositoryCollection.findByName(name);
    }

    @Transactional
    public Collection updateCollection(Collection updatedCollection) {
        return repositoryCollection.save(updatedCollection);
    }

    @Transactional
    public void deleteCollection(Integer id) {
        repositoryCollection.deleteById(id);
    }

    @Transactional
    public void deleteCollectionByName(String name) {
        Optional<Collection> collection = repositoryCollection.findByName(name);
        collection.ifPresent(repositoryCollection::delete);
    }

}
