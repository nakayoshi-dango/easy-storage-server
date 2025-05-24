package com.izanyfran.easy_storage.dto;

import java.util.Date;

public class CollectionDTO {

    private Integer id;
    private String name;
    private String description;
    private String ownerUsername;
    private Date creationDate;

    public CollectionDTO(Integer id, String name, String description, String ownerUsername, Date creationDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerUsername = ownerUsername;
        this.creationDate = creationDate;
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
