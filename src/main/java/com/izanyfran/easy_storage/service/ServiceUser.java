package com.izanyfran.easy_storage.service;

import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.repository.RepositoryUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceUser {

    @Autowired
    private RepositoryUser repositoryUser;
    
    @Transactional
    public User createUser(User user) {
        return repositoryUser.save(user);
    }

    public List<User> getAllUsers() {
        return repositoryUser.findAll();
    }

    public User getUserById(Integer id) {
        return repositoryUser.findById(id).orElse(null);
    }
    
    public User getUserByUsername(String nombreLogin) {
        return repositoryUser.getUserByUsername(nombreLogin);
    }

    public User updateUser(User updatedUser) {
        return repositoryUser.save(updatedUser);
    }

    public void deleteUser(Integer id) {
        repositoryUser.deleteById(id);
    }

    public void deleteUserByUsername(String nombreLogin) {
        // Find the user by name
        User user = repositoryUser.getUserByUsername(nombreLogin);

        if (user != null) {
            // Delete the user from the repository
            repositoryUser.delete(user);
        } 
    }

}
