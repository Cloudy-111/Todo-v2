package com.example.todo_listv2.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todo_listv2.databinding.ActivityDetailTaskBinding;

public class DetailTaskActivity extends AppCompatActivity {
    private ActivityDetailTaskBinding binding;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = preferences.getString("user_id", "0");


    }
}
