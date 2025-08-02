package com.example.todo_listv2.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.todo_listv2.activities.LoginActivity;
import com.example.todo_listv2.databinding.FragmentProfileBinding;
import com.example.todo_listv2.viewModels.AuthViewModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.material.card.MaterialCardView;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private SharedPreferences preferences;
    private String userId;
    private TextView namePrf;
    private ImageView avatarImage;
    private MaterialCardView changeAvatarButton;
    private FrameLayout editPrfButton, settingButton;
    private LinearLayout signOutButton, changePasswordButton;
    private AuthViewModel authViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = preferences.getString("user_id", "0");
        authViewModel = new ViewModelProvider(getActivity()).get(AuthViewModel.class);

        initViews();
        observeData();
        setOnClickListener();

        authViewModel.loadDataUser(userId);
    }

    private void initViews(){
        namePrf = binding.namePrf;
        avatarImage = binding.avatarPrf;
        settingButton = binding.settingButton;
        editPrfButton = binding.editProfileButton;
        signOutButton = binding.logoutButton;
        changePasswordButton = binding.changePasswordButton;
        changeAvatarButton = binding.changeAvatarButton;
    }

    private void observeData(){
        authViewModel.user.observe(getViewLifecycleOwner(), user -> {
            if(user != null){
                namePrf.setText(user.getUsername());
                Glide.with(getContext())
                        .load(user.getAvatar())
                        .into(avatarImage);
            }
        });
    }

    private void setOnClickListener(){
        settingButton.setOnClickListener(v -> {

        });

        editPrfButton.setOnClickListener(v -> {

        });

        signOutButton.setOnClickListener(v -> {
            preferences.edit().putString("user_id", "0").apply();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        });

        changeAvatarButton.setOnClickListener(v -> {

        });

        changePasswordButton.setOnClickListener(v -> {

        });
    }
}
