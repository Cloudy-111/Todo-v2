package com.example.todo_listv2.repositories;

import com.example.todo_listv2.fake.fakeDB;
import com.example.todo_listv2.models.Priority;

import java.util.List;

import okhttp3.OkHttpClient;

public class PriorityRepository {
    private OkHttpClient client = new OkHttpClient();
    private final String baseURL = "http://192.168.10.105:5000";

    public List<Priority> getAllPriority(){
        return fakeDB.getAllPriority();
    }
}
