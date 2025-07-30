package com.example.todo_listv2.models;

import java.util.UUID;

public class Note {
    private String id;
    private String header;
    private String content;
    private String color;
    private int backgroundId;
    private String userId;

    public Note(String header, String content, String color, int backgroundId, String userId) {
        this.id = UUID.randomUUID().toString();
        this.header = header;
        this.content = content;
        this.color = color;
        this.backgroundId = backgroundId;
        this.userId = userId;
    }

    public Note(String id, String header, String content, String color, int backgroundId, String userId) {
        this.id = id;
        this.header = header;
        this.content = content;
        this.color = color;
        this.backgroundId = backgroundId;
        this.userId = userId;
    }

    public Note() {}

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
    }
}
