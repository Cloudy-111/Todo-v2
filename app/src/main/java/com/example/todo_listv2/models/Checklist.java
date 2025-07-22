package com.example.todo_listv2.models;

import java.util.UUID;

public class Checklist {
    private String id;
    private String content;
    private boolean isCompleted;
    private String taskId;

    public Checklist(String content) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.isCompleted = false;
        this.taskId = "-1";
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
