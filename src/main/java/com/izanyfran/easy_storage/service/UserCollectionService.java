package com.izanyfran.easy_storage.service;

import com.izanyfran.easy_storage.entity.Collection;
import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.entity.UserCollection;
import com.izanyfran.easy_storage.repository.CollectionRepository;
import com.izanyfran.easy_storage.repository.UserCollectionRepository;
import com.izanyfran.easy_storage.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserCollectionService {

    @Autowired
    private UserCollectionRepository userCollectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    public List<User> getUsersByCollectionId(Integer collectionId) {
        return userCollectionRepository.findUsersByCollectionId(collectionId);
    }

    public List<User> getUsersByCollectionName(String collectionName) {
        return userCollectionRepository.findUsersByCollectionName(collectionName);
    }
    
    public List<User> getUsersNotInCollection(String collectionName) {
        return userCollectionRepository.findUsersNotInCollection(collectionName);
    }

    public List<Collection> getCollectionsByUserId(Integer userId) {
        return userCollectionRepository.findCollectionsByUserId(userId);
    }

    public List<Collection> getCollectionsByUserName(String userName) {
        return userCollectionRepository.findCollectionsByUserName(userName);
    }

    public boolean addUserToCollection(Integer userId, Integer collectionId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Collection> collectionOpt = collectionRepository.findById(collectionId);

        Optional<UserCollection> relationOpt = userCollectionRepository.findRelation(userId, collectionId);
        if (relationOpt.isPresent()) {
            return false; // ya era miembro colección
        }

        UserCollection userCollection = new UserCollection(userOpt.get(), collectionOpt.get());
        userCollectionRepository.save(userCollection);
        return true;// añadido a la colección
    }

    public boolean removeUserFromCollection(Integer userId, Integer collectionId) {
        Optional<UserCollection> relationOpt = userCollectionRepository.findRelation(userId, collectionId);

        if (relationOpt.isPresent()) {
            userCollectionRepository.delete(relationOpt.get());
            return true;//borrado
        } else {
            return false;//no borrado
        }
    }
}
