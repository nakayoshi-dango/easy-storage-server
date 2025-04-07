package com.izanyfran.easy_storage.repository;

import com.izanyfran.easy_storage.entity.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryCollection extends JpaRepository<Collection, Integer> {
    Optional<Collection> getCollectionByName(String collectionName);
}
