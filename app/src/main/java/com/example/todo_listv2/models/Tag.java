package com.example.todo_listv2.models;

import java.util.UUID;

public class Tag {
    private String id;
    private String color;
    private String name;
    private String userId;

    public Tag(String userId, String color, String name) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.color = color;
        this.name = name;
    }

    // Test
    public Tag(String id, String userId, String color, String name) {
        this.id = id;
        this.userId = userId;
        this.color = color;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
