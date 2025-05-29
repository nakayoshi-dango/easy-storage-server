package com.izanyfran.easy_storage.repository;

import com.izanyfran.easy_storage.entity.Product;
import com.izanyfran.easy_storage.entity.ProductCollection;
import com.izanyfran.easy_storage.entity.ProductCollectionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProductCollectionRepository extends JpaRepository<ProductCollection, ProductCollectionKey> {
    @Query("SELECT pc FROM ProductCollection pc WHERE pc.product.id = :productId")
    List<ProductCollection> findRelationsByProductId(String productId);
    
    @Query("SELECT pc FROM ProductCollection pc WHERE pc.collection.name = :collectionName")
    List<ProductCollection> findRelationsByCollectionName(String collectionName);
    
    @Query("SELECT pc FROM ProductCollection pc WHERE pc.collection.name = :collectionName AND pc.product.id = :productId")
    Optional<ProductCollection> findRelation(String collectionName, String productId);
    
    @Query("SELECT pc.product FROM ProductCollection pc WHERE pc.collection.id = :collectionId")
    List<Product> findProductsByCollectionId(@Param("collectionId") Integer collectionId);
    @Query("SELECT pc.product FROM ProductCollection pc WHERE pc.collection.name = :collectionName")
    List<Product> findProductsByCollectionName(@Param("collectionName") String collectionName);
}
