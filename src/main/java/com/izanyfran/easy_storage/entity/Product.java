package com.izanyfran.easy_storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product implements Serializable {

    //Barcode raw value as product ID
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "uploader_id", referencedColumnName = "id", nullable = false)
    private User uploader;

    @Column(name = "where_to_buy")
    private String whereToBuy;
    
    @Column(name = "upload_date")
    private Date uploadDate = new Date();
    
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageURL;
    
    
    public Product() {
    }
    
    public Product(String id, String name, String description, String whereToBuy, String imageURL) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.whereToBuy = whereToBuy;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.description);
        hash = 97 * hash + Objects.hashCode(this.uploader);
        hash = 97 * hash + Objects.hashCode(this.whereToBuy);
        hash = 97 * hash + Objects.hashCode(this.uploadDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.whereToBuy, other.whereToBuy)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.uploader, other.uploader)) {
            return false;
        }
        return Objects.equals(this.uploadDate, other.uploadDate);
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", description=" + description + ", uploader=" + uploader.getUsername() + ", whereToBuy=" + whereToBuy + ", uploadDate=" + uploadDate + '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    public String getWhereToBuy() {
        return whereToBuy;
    }

    public void setWhereToBuy(String whereToBuy) {
        this.whereToBuy = whereToBuy;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
}
