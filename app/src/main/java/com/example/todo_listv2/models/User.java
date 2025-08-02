package com.example.todo_listv2.models;

import java.util.UUID;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private String avatar;

    public User() {}

    public User(String id, String username, String email, String password, String avatar) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
    }

    public static User createNewUser(String username, String email, String password, String avatar){
        return new User(
                UUID.randomUUID().toString(),
                username,
                email,
                password,
                avatar);
    }

    public static User loadUser(String id, String username, String email, String avatar){
        return new User(id, username, email, null, avatar);
    }

    public static User loadUserWithPassword(String id, String username, String email, String password, String avatar){
        return new User(id, username, email, password, avatar);
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
