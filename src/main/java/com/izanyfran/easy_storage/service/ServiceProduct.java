package com.izanyfran.easy_storage.service;

import com.izanyfran.easy_storage.entity.Product;
import com.izanyfran.easy_storage.repository.RepositoryProduct;
import java.util.List;
import java.util.Optional;
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

    public Optional<Product> getProductById(String id) {
        return repositoryProduct.findById(id);
    }
    
    public Optional<Product> getProductByName(String name) {
        return repositoryProduct.getProductByName(name);
    }
    
    @Transactional
    public Product updateProduct(Product updatedProduct) {
        return repositoryProduct.save(updatedProduct);
    }
    
    @Transactional
    public void deleteProduct(String id) {
        repositoryProduct.deleteById(id);
    }
    
    @Transactional
    public void deleteProductByName(String name) {
        Optional<Product> product = repositoryProduct.getProductByName(name);
        product.ifPresent(repositoryProduct::delete);
    }

}
