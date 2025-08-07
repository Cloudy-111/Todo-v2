package com.example.todo_listv2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todo_listv2.BuildConfig;
import com.example.todo_listv2.R;
import com.example.todo_listv2.databinding.ActivityForgotPasswordBinding;
import com.example.todo_listv2.repositories.AuthRepository;
import com.google.android.material.textfield.TextInputEditText;

import org.checkerframework.checker.units.qual.A;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    private TextInputEditText emailInput;
    private Button submitButton;
    private ImageView backButton;
    private AuthRepository authRepository;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        authRepository = new AuthRepository();

        initViews();
        setOnClickListener();
    }

    private void initViews(){
        emailInput = binding.emailEditText;
        submitButton = binding.resetPasswordButton;
        backButton = binding.backButton;
    }

    private void setOnClickListener(){
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            overridePendingTransition(0, R.anim.slide_out_right);
            finish();
        });

        submitButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            new Thread(() -> {
                try{
                    GmailSender sender = new GmailSender(BuildConfig.EMAIL_SEND_NEW_PASSWORD, BuildConfig.APP_PASSWORD); // app password
                    String newPass = generateRandomPassword();
                    String to = email;
                    String subject = "Reset Password";
                    String body = "Your temp password: " + newPass;

                    sender.sendEmail(to, subject, body);

                    authRepository.resetPassword(email, newPass, new AuthRepository.OnSendMailResetPasswordCallback() {
                        @Override
                        public void onSuccess(String messenger) {
                            Toast.makeText(ForgotPasswordActivity.this, "Password has been send to email: " + email, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(String errorMessenger) {
                            Toast.makeText(ForgotPasswordActivity.this, errorMessenger, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        });
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
}
