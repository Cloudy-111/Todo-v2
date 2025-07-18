package com.example.todo_listv2.models;

public class Priority {
    private String id;
    private String name;
    private String colorHex;
    private int level;

    public Priority(String name, String colorHex, int level) {
        this.name = name;
        this.colorHex = colorHex;
        this.level = level;
    }

    // Test DB
    public Priority(String id, String name, String colorHex, int level) {
        this.id = id;
        this.name = name;
        this.colorHex = colorHex;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
