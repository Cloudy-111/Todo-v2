package com.example.todo_listv2.models;

public class Tag {
    private String id;
    private String color;
    private String name;
    private int userId;

    public Tag(int userId, String color, String name) {
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
