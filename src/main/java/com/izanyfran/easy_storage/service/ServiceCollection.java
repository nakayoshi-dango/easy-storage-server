package com.izanyfran.easy_storage.service;

import com.izanyfran.easy_storage.entity.Collection;
import com.izanyfran.easy_storage.repository.RepositoryCollection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceCollection {

    @Autowired
    private RepositoryCollection repositoryCollection;
    
    @Transactional
    public Collection createCollection(Collection collection) {
        return repositoryCollection.save(collection);
    }

    public List<Collection> getAllCollections() {
        return repositoryCollection.findAll();
    }

    public Optional<Collection> getCollectionById(Integer id) {
        return repositoryCollection.findById(id);
    }
    
    public Optional<Collection> getCollectionByName(String name) {
        return repositoryCollection.getCollectionByName(name);
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
        Optional<Collection> collection = repositoryCollection.getCollectionByName(name);
        collection.ifPresent(repositoryCollection::delete);
    }

}
