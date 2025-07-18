package com.example.todo_listv2.repositories;

import com.example.todo_listv2.fake.fakeDB;
import com.example.todo_listv2.models.Tag;

import java.util.List;

import okhttp3.OkHttpClient;

public class TagRepository {
    private OkHttpClient client = new OkHttpClient();
    private final String baseURL = "http://192.168.10.105:5000";
    public List<Tag> getAllTagByUserId(String userId){
        return fakeDB.getAllTag("1");
    }
}
