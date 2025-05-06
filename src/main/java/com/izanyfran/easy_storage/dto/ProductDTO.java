package com.izanyfran.easy_storage.dto;

import java.sql.Date;

public class ProductDTO {
    private String id;
    private String name;
    private String description;
    private String uploaderUsername;
    private String whereToBuy;
    private Date uploadDate;
    private String imageURL;

    public ProductDTO(String id, String name, String description, String uploaderUsername, String whereToBuy, Date uploadDate, String imageURL) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.uploaderUsername = uploaderUsername;
        this.whereToBuy = whereToBuy;
        this.uploadDate = uploadDate;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
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

    public String getImageURL() {
        return imageURL;
    }
}
