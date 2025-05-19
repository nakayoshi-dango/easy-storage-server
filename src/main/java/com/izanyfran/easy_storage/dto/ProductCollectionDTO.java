package com.izanyfran.easy_storage.dto;

import java.util.Date;

public class ProductCollectionDTO {

    private String productId;
    private String name;
    private String description;
    private String uploaderUsername;
    private String whereToBuy;
    private Date uploadDate;
    private int quantity;

    public ProductCollectionDTO(String productId, String name, String description, String uploaderUsername, String whereToBuy, Date uploadDate, int quantity) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.uploaderUsername = uploaderUsername;
        this.whereToBuy = whereToBuy;
        this.uploadDate = uploadDate;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUploaderUsername() {
        return uploaderUsername;
    }

    public String getWhereToBuy() {
        return whereToBuy;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public int getQuantity() {
        return quantity;
    }
}
