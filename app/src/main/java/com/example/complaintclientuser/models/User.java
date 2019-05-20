package com.example.complaintclientuser.models;

import java.sql.Timestamp;

public class User {

    private Integer id;
    private Role role;
    private Instance instance;
    private String name;
    private String email;
    private String username;
    private Timestamp created_at;
    private Timestamp updated_at;

    public User() {
    }

    public User(Integer id, String name, String email, String username, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public Role getRole() {
        return role;
    }

    public Instance getInstance() {
        return instance;
    }
}
