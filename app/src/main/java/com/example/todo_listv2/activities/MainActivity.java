package com.example.todo_listv2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.todo_listv2.R;
import com.example.todo_listv2.databinding.ActivityMainBinding;
import com.example.todo_listv2.fragments.CalendarFragment;
import com.example.todo_listv2.fragments.HomeFragment;
import com.example.todo_listv2.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = preferences.getString("user_id", "0");

        HomeFragment homeFragment = new HomeFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        replaceFragment(homeFragment);
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeBtn) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.profileBtn) {
                replaceFragment(new ProfileFragment());
            } else if (id == R.id.calendarBtn){
                replaceFragment(new CalendarFragment());
            }
            return true;
        });

        binding.addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_animation);
                finish();
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }
}