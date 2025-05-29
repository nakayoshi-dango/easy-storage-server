package com.izanyfran.easy_storage.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_collections")
public class UserCollection {

    @EmbeddedId
    private UserCollectionKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("collectionId")
    @JoinColumn(name = "collection_id")
    private Collection collection;

    public UserCollection() {
    }

    public UserCollection(User user, Collection collection) {
        this.id = new UserCollectionKey();
        this.id.setUserId(user.getId());
        this.id.setCollectionId(collection.getId());
        this.user = user;
        this.collection = collection;
    }

    public UserCollectionKey getId() {
        return id;
    }

    public void setId(UserCollectionKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }
    
    

}
