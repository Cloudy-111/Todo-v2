package com.example.todo_listv2.models;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.UUID;

public class Task {
    private String id;
    private String title;
    private String description;
    private String note;
    private long startTime;
    private long endTime;
    private long remindAt;
    private long createAt;
    private boolean isCompleted;
    private long completedAt;
    private String priorityId;
    private String tagId;
    private boolean isProgressTask;
    private double successRate;
    private String userId;

    public Task(String title, String description, String note, long startTime, long endTime, long remindAt, String priorityId, String tagId, boolean isProgressTask, double successRate, String userId) {
        this.id = UUID.randomUUID().toString();
        this.note = note;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.remindAt = remindAt;
        this.isCompleted = false;
        this.completedAt = 0;
        this.createAt = LocalDate.now().getLong(ChronoField.EPOCH_DAY);
        this.priorityId = priorityId;
        this.tagId = tagId;
        this.isProgressTask = isProgressTask;
        this.successRate = successRate;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public boolean isProgressTask() {
        return isProgressTask;
    }

    public void setProgressTask(boolean progressTask) {
        isProgressTask = progressTask;
    }

    public String getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(String priorityId) {
        this.priorityId = priorityId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(long remindAt) {
        this.remindAt = remindAt;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }
}
