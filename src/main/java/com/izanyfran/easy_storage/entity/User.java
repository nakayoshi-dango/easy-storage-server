package com.izanyfran.easy_storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private Integer passwordHash;

    @Column(nullable = false)
    private String role;

    @Column(name = "phone_number", unique = true, nullable = false)
    private Integer phoneNumber;
    
    @Column(name = "creation_date")
    private Date creationDate;

    public User() {
    }

    public User(String username, Integer passwordHash, String role, Integer phoneNumber, Date createdAt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.creationDate = createdAt;
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.username);
        hash = 67 * hash + Objects.hashCode(this.passwordHash);
        hash = 67 * hash + Objects.hashCode(this.role);
        hash = 67 * hash + Objects.hashCode(this.phoneNumber);
        hash = 67 * hash + Objects.hashCode(this.creationDate);
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
        final User other = (User) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.passwordHash, other.passwordHash)) {
            return false;
        }
        if (!Objects.equals(this.role, other.role)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.phoneNumber, other.phoneNumber)) {
            return false;
        }
        return Objects.equals(this.creationDate, other.creationDate);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", passwordHash=" + passwordHash + ", role=" + role + ", phoneNumber=" + phoneNumber + ", createdAt=" + creationDate + '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(Integer passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getCreatedAt() {
        return creationDate;
    }

    public void setCreatedAt(Date createdAt) {
        this.creationDate = createdAt;
    }
    
}
