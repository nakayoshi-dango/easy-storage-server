package com.izanyfran.easy_storage.repository;

import com.izanyfran.easy_storage.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryUser extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
