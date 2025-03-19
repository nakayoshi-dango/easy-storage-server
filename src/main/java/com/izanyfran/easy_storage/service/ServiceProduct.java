package com.izanyfran.easy_storage.service;

import com.izanyfran.easy_storage.entity.Product;
import com.izanyfran.easy_storage.repository.RepositoryProduct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceProduct {

    @Autowired
    private RepositoryProduct repositoryProduct;
    
    @Transactional
    public Product createProduct(Product product) {
        return repositoryProduct.save(product);
    }

    public List<Product> getAllProducts() {
        return repositoryProduct.findAll();
    }

    public Product getProductById(String id) {
        return repositoryProduct.findById(id).orElse(null);
    }
    
    public Product getProductByName(String name) {
        return repositoryProduct.getProductByName(name);
    }

    public Product updateProduct(Product updatedProduct) {
        return repositoryProduct.save(updatedProduct);
    }

    public void deleteProduct(String id) {
        repositoryProduct.deleteById(id);
    }

    public void deleteProductByName(String name) {
        // Find the product by name
        Product product = repositoryProduct.getProductByName(name);

        if (product != null) {
            // Delete the product from the repository
            repositoryProduct.delete(product);
        } 
    }

}
