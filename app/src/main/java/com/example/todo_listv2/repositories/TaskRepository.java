package com.example.todo_listv2.repositories;

import com.example.todo_listv2.fake.fakeDB;
import com.example.todo_listv2.models.Task;

import java.util.List;

import okhttp3.OkHttpClient;

public class TaskRepository {
    private OkHttpClient client = new OkHttpClient();
    private final String baseURL = "http://192.168.10.105:5000";

    public List<Task> getAllTaskByUserIdAndSelectedDay(String day, String userId){
        return fakeDB.getAllTaskByUserIdAndSelectedDay(day, userId);
    }
}
