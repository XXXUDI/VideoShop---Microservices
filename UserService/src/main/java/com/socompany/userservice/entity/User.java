package com.socompany.userservice.entity;


import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


// TODO: rework entity using Role

@Entity
@Data
public class User {


    @Id @GeneratedValue
    private long id;

    @Column(unique=true, nullable=false) 
    private String username;
    @Column(nullable=false)
    private String password;
    @Column(unique=true, nullable=false)
    private String email;

    @CreationTimestamp @Column(updatable = false)
    private Timestamp created;
    @UpdateTimestamp
    private Timestamp updated;

    public User(String username, String password, String email, Timestamp updated) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.updated = updated;
    }

    public User() {}
}
