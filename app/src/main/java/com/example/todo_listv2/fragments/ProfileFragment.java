package com.example.todo_listv2.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.cloudinary.android.callback.UploadCallback;
import com.example.todo_listv2.R;
import com.example.todo_listv2.activities.LoginActivity;
import com.example.todo_listv2.databinding.FragmentProfileBinding;
import com.example.todo_listv2.repositories.AuthRepository;
import com.example.todo_listv2.viewModels.AuthViewModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;

public class ProfileFragment extends Fragment{
    private FragmentProfileBinding binding;
    private SharedPreferences preferences;
    private String userId;
    private TextView namePrf;
    private EditText inputOldPassword, inputNewPassword, inputConfirmNewPassword;
    private Button submitChangePassword;
    private ImageView avatarImage;
    private MaterialCardView changeAvatarButton;
    private FrameLayout editPrfButton, settingButton;
    private LinearLayout signOutButton, changePasswordButton;
    private AuthViewModel authViewModel;
    private ActivityResultLauncher<Intent> mGetContent;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String stringAvatar;
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

        mGetContent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        Glide.with(getContext())
                                .load(imageUri)
                                .into(avatarImage);

                        uploadImageToCloudinary(imageUri);
                    }
                }
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(getContext())
                    .load(imageUri)
                    .into(avatarImage);
            uploadImageToCloudinary(imageUri);
        }
    }

    private void observeData(){
        authViewModel.user.observe(getViewLifecycleOwner(), user -> {
            if(user != null){
                namePrf.setText(user.getUsername());
                Glide.with(getContext())
                        .load(user.getAvatar())
                        .into(avatarImage);
                stringAvatar = user.getAvatar();
            }
        });
    }

    private void setOnClickListener(){
        settingButton.setOnClickListener(v -> {

        });

        editPrfButton.setOnClickListener(v -> {

        });

        signOutButton.setOnClickListener(v -> {
            String authType = preferences.getString("auth_type", "normal");
            if ("google".equals(authType)) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

                mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
                    clearSessionAndGoToLogin();
                });
            } else {
                clearSessionAndGoToLogin();
            }
        });

        changeAvatarButton.setOnClickListener(v -> {
            openImageChooser();
        });

        changePasswordButton.setOnClickListener(v -> {
            showDialogChangePassword();
        });
    }

    private void clearSessionAndGoToLogin(){
        preferences.edit()
                .putString("user_id", "0")
                .putString("auth_type", "")
                .apply();

        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void openImageChooser(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        mGetContent.launch(intent);
    }

    private void uploadImageToCloudinary(Uri imageUri){
        authViewModel.uploadImageToCloudinary(requireContext(), imageUri, userId, stringAvatar);
    }

    private void showDialogChangePassword(){
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_reset_password, null);
        dialog.setContentView(view);

        inputOldPassword = view.findViewById(R.id.input_old_password);
        inputNewPassword = view.findViewById(R.id.input_new_password);
        inputConfirmNewPassword = view.findViewById(R.id.input_confirm_new_password);
        submitChangePassword = view.findViewById(R.id.button_submit_change_password);

        submitChangePassword.setOnClickListener(v -> {
            String oldPassword = inputOldPassword.getText().toString().trim();
            String newPassword = inputNewPassword.getText().toString().trim();
            String confirmNewPassword = inputConfirmNewPassword.getText().toString().trim();


            if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmNewPassword)) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 6) {
                Toast.makeText(getContext(), "Mật khẩu mới phải có ít nhất 6 ký tự.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmNewPassword)) {
                Toast.makeText(getContext(), "Mật khẩu mới không khớp.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (oldPassword.equals(newPassword)) {
                Toast.makeText(getContext(), "Mật khẩu mới phải khác mật khẩu cũ.", Toast.LENGTH_SHORT).show();
                return;
            }

            authViewModel.updatePassword(oldPassword, newPassword, userId, new AuthRepository.OnPasswordResetCallback() {
                @Override
                public void onSuccess() {
                    Log.d("Auth", "Update Password Successfully");

                    // This is background thread, so use handler to change to UI thread to use Toast to update UI
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(requireContext(), "Update Password Successfully", Toast.LENGTH_SHORT).show();
                    });
                    dialog.dismiss();
                }

                @Override
                public void onFailure(String errorMessage) {
                    Log.e("Auth_error", errorMessage);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });

        dialog.show();
    }
}
