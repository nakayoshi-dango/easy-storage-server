package com.izanyfran.easy_storage.service;

import com.izanyfran.easy_storage.dto.UserDTO;
import com.izanyfran.easy_storage.entity.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import com.izanyfran.easy_storage.repository.UserRepository;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repositoryUser;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repositoryUser) {
        this.repositoryUser = repositoryUser;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public Boolean registerUser(String username, String password) {
        Boolean success;
        if (username.equalsIgnoreCase("admin")) {
            return false;
        }
        if (getUserByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password)); // Guardar el hash
            user.setImageURL("https://cdn.pixabay.com/photo/2023/11/24/10/16/duck-8409656_1280.png");
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

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getRole(), user.getCreatedAt(), user.getImageURL());
    }

    public List<UserDTO> toDTOList(List<User> users) {
        return users.stream().map(this::toDTO).collect(Collectors.toList());
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
