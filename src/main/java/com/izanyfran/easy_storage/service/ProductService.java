package com.izanyfran.easy_storage.service;

import com.izanyfran.easy_storage.dto.ProductDTO;
import com.izanyfran.easy_storage.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.repository.UserRepository;
import com.izanyfran.easy_storage.repository.ProductRepository;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repositoryProduct;
    @Autowired
    private UserRepository repositoryUser;

    @Transactional
    public Product createProduct(Product product) {
        return repositoryProduct.save(product);
    }

    public List<Product> getAllProducts() {
        return repositoryProduct.findAll();
    }

    public List<Product> getUserProducts(String username) {
        Optional<User> user = repositoryUser.findByUsername(username);
        if (user.isPresent()) {
            User uploader = user.get();
            return repositoryProduct.findByUploader(uploader);
        } else {
            return List.of();
        }
    }
    
    // 🔹 Convertir Product a ProductDTO
    public ProductDTO toDTO(Product product) {
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getUploader().getUsername(), // Extrae solo el nombre del uploader
            product.getWhereToBuy(),
            product.getUploadDate(),
            product.getImageURL()
        );
    }

    public List<ProductDTO> toDTOList(List<Product> products) {
        return products.stream().map(this::toDTO).collect(Collectors.toList());
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
