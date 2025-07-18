package com.example.todo_listv2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todo_listv2.R;
import com.example.todo_listv2.databinding.ActivitySignupBinding;
import com.example.todo_listv2.models.User;
import com.example.todo_listv2.repositories.AuthRepository;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private SharedPreferences preferences;
    private final String default_avatar = "https://as1.ftcdn.net/v2/jpg/12/19/99/12/1000_F_1219991294_jxrVosZzEULp0NZSp6DDs5W3es5gcP16.jpg";
    private AuthRepository authRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authRepository = new AuthRepository(this);
        binding.loginNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.usernameEditText.getText().toString().trim();
                String email = binding.emailEditText.getText().toString().trim();
                String password = binding.passwordEditText.getText().toString().trim();

                if(!username.isEmpty() || !password.isEmpty()){
                    signupUser(username, password, email, default_avatar);
                } else {
                    Toast.makeText(SignupActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signupUser(String username, String password, String email, String image_avatar){
        User newUser = new User(username, email, password, image_avatar);
        authRepository.register(newUser, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(SignupActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                    runOnUiThread(() -> {
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(SignupActivity.this, error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
