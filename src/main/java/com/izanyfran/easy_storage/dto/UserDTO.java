package com.izanyfran.easy_storage.dto;

import java.sql.Date;

public class UserDTO {

    private Integer id;
    private String username;
    private String role;
    private Date creationDate;

    public UserDTO(Integer id, String username, String role, Date creationDate) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.creationDate = creationDate;
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
