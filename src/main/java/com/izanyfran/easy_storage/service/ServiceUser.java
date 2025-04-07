package com.izanyfran.easy_storage.service;

import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.repository.RepositoryUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

@Service
public class ServiceUser {

    private final RepositoryUser repositoryUser;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public ServiceUser(RepositoryUser repositoryUser) {
        this.repositoryUser = repositoryUser;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public Boolean registerUser(String username, String password) {
        Boolean success;
        if (getUserByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password)); // Guardar el hash
            repositoryUser.save(user);
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    public boolean authenticate(String username, String password) {
        Optional<User> user = repositoryUser.findByUsername(username);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }

    @Transactional
    public User createUser(User user) {
        return repositoryUser.save(user);
    }

    public List<User> getAllUsers() {
        return repositoryUser.findAll();
    }

    public String getUserRole(String username) {
        return repositoryUser.findByUsername(username).get().getRole();
    }

    public Optional<User> getUserById(Integer id) {
        return repositoryUser.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return repositoryUser.findByUsername(username);
    }

    @Transactional
    public User updateUser(User updatedUser) {
        return repositoryUser.save(updatedUser);
    }

    @Transactional
    public void deleteUserById(Integer id) {
        repositoryUser.deleteById(id);
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        Optional<User> user = repositoryUser.findByUsername(username);
        user.ifPresent(repositoryUser::delete);
    }

}
