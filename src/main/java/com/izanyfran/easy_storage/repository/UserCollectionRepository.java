package com.izanyfran.easy_storage.repository;

import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.entity.Collection;
import com.izanyfran.easy_storage.entity.UserCollection;
import com.izanyfran.easy_storage.entity.UserCollectionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserCollectionRepository extends JpaRepository<UserCollection, UserCollectionKey> {

    @Query("SELECT uc.user FROM UserCollection uc WHERE uc.collection.id = :collectionId")
    List<User> findUsersByCollectionId(@Param("collectionId") Integer collectionId);

    @Query("SELECT uc.user FROM UserCollection uc WHERE uc.collection.name = :collectionName")
    List<User> findUsersByCollectionName(@Param("collectionName") String collectionName);

    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT uc.user.id FROM UserCollection uc WHERE uc.collection.name = :collectionName)")
    List<User> findUsersNotInCollection(@Param("collectionName") String collectionName);

    @Query("SELECT uc.collection FROM UserCollection uc WHERE uc.user.id = :userId")
    List<Collection> findCollectionsByUserId(@Param("userId") Integer userId);

    @Query("SELECT uc.collection FROM UserCollection uc WHERE uc.user.username = :username")
    List<Collection> findCollectionsByUserName(@Param("username") String username);

    @Query("SELECT uc FROM UserCollection uc WHERE uc.user.id = :userId AND uc.collection.id = :collectionId")
    Optional<UserCollection> findRelation(@Param("userId") Integer userId, @Param("collectionId") Integer collectionId);
}
