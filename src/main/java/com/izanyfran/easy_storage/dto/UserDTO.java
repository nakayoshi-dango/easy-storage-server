package com.izanyfran.easy_storage.dto;

import java.util.Date;

public class UserDTO {

    private Integer id;
    private String username;
    private String role;
    private Date creationDate;
    private String imageURL;

    public UserDTO(Integer id, String username, String role, Date creationDate, String imageURL) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.creationDate = creationDate;
        this.imageURL = imageURL;
    }
    
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
