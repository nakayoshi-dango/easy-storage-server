package com.izanyfran.easy_storage.dto;

import java.util.Date;

public class CollectionDTO {

    private Integer id;
    private String name;
    private String description;
    private String ownerUsername;
    private Date creationDate;
    private String imageURL;
    
    public CollectionDTO(Integer id, String name, String description, String ownerUsername, Date creationDate, String imageURL) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerUsername = ownerUsername;
        this.creationDate = creationDate;
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "CollectionDTO{"
                + "\n\tid=" + id
                + "\n\tname=" + name
                + "\n\tdescription=" + description
                + "\n\townerUsername=" + ownerUsername
                + "\n\tcreationDate=" + creationDate
                + "\n}";
    }
    
    public String getImageURL() {
        return imageURL;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
