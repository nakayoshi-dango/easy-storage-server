package com.izanyfran.easy_storage.repository;

import com.izanyfran.easy_storage.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryProduct extends JpaRepository<Product, String> {
    Product getProductByName(String productName);
}
