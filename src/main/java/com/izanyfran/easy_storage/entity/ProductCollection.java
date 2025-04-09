package com.izanyfran.easy_storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "products_collections")
public class ProductCollection {
    
    @EmbeddedId
    private ProductCollectionKey id;
    
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;
    
    @ManyToOne
    @MapsId("collectionId")
    @JoinColumn(name = "collection_id")
    private Collection collection;
    
    @Column(nullable = false)
    private int quantity=1;

    public ProductCollection() {
    }
    
    public ProductCollection(Product product, Collection collection, int quantity) {
    this.id = new ProductCollectionKey();
    this.id.setProductId(product.getId());
    this.id.setCollectionId(collection.getId());
    this.product = product;
    this.collection = collection;
    this.quantity = quantity;
}

    public ProductCollectionKey getId() {
        return id;
    }

    public void setId(ProductCollectionKey id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    
}
