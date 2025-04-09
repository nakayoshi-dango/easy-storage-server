package com.izanyfran.easy_storage.repository;

import com.izanyfran.easy_storage.entity.Product;
import com.izanyfran.easy_storage.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByUploader(User uploader);
    Optional<Product> getProductByName(String productName);
}
