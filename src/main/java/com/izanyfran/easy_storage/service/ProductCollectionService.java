package com.izanyfran.easy_storage.service;

import com.izanyfran.easy_storage.dto.ProductCollectionDTO;
import com.izanyfran.easy_storage.entity.Collection;
import com.izanyfran.easy_storage.entity.Product;
import com.izanyfran.easy_storage.entity.ProductCollection;
import com.izanyfran.easy_storage.repository.CollectionRepository;
import com.izanyfran.easy_storage.repository.ProductCollectionRepository;
import com.izanyfran.easy_storage.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCollectionService {

    @Autowired
    private ProductCollectionRepository productCollectionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    public List<Product> getProductsByCollectionId(Integer collectionId) {
        return productCollectionRepository.findProductsByCollectionId(collectionId);
    }

    public List<Product> getProductsByCollectionName(String collectionName) {
        return productCollectionRepository.findProductsByCollectionName(collectionName);
    }

    public List<ProductCollection> getRelationsByProductId(String productId) {
        return productCollectionRepository.findRelationsByProductId(productId);
    }

    public List<ProductCollection> getRelationsByCollectionName(String collectionName) {
        return productCollectionRepository.findRelationsByCollectionName(collectionName);
    }
    
    public Optional<ProductCollection> getRelation(String collectionName, String productId) {
        return productCollectionRepository.findRelation(collectionName, productId);
    }
    
    public Boolean isProductInCollection(String productId, String collectionName) {
        return productCollectionRepository.findRelation(collectionName, productId).isPresent();
    }

    public ProductCollection addProductToCollection(String productId, String collectionName, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Collection collection = collectionRepository.findByName(collectionName)
                .orElseThrow(() -> new RuntimeException("Colección no encontrada"));

        ProductCollection productCollection = new ProductCollection(product, collection, quantity);
        return productCollectionRepository.save(productCollection);
    }
    
    @Transactional
    public ProductCollection updateRelation(ProductCollection relation) {
        return productCollectionRepository.save(relation);
    }
    
    @Transactional
    public void deleteRelation(ProductCollection relation) {
        productCollectionRepository.delete(relation);
    }
    
    // 🔹 Convertir Product a ProductDTO
    public ProductCollectionDTO toDTO(ProductCollection productCollection) {
        return new ProductCollectionDTO(
            productCollection.getProduct().getId(),
            productCollection.getProduct().getName(),
            productCollection.getProduct().getDescription(),
            productCollection.getProduct().getUploader().getUsername(), // Extrae solo el nombre del uploader
            productCollection.getProduct().getWhereToBuy(),
            productCollection.getProduct().getUploadDate(),
            productCollection.getQuantity()
        );
    }

    public List<ProductCollectionDTO> toDTOList(List<ProductCollection> productCollections) {
        return productCollections.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
