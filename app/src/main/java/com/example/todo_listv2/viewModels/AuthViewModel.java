package com.example.todo_listv2.viewModels;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.todo_listv2.models.User;
import com.example.todo_listv2.repositories.AuthRepository;
import com.example.todo_listv2.repositories.ImageRepository;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository = new AuthRepository();
    private ImageRepository imageRepository = new ImageRepository();

    private MutableLiveData<User> _user = new MutableLiveData<>();
    public LiveData<User> user = _user;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void loadDataUser(String userId){
        executor.execute(() -> {
            User result = authRepository.loadUser(userId);

            _user.postValue(result);
        });
    }

    public void uploadImageToCloudinary(Context context, Uri imageUri, String userId, String oldStringAvatar){
        imageRepository.uploadImage(context, imageUri, new UploadCallback() {
            @Override
            public void onStart(String requestId) {

            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {

            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                String imageUrl = (String) resultData.get("secure_url");
                authRepository.updateProfileImageUrl(imageUrl, userId, new AuthRepository.OnAvatarUpdatedCallback() {
                    @Override
                    public void onSuccess() {
                        imageRepository.deleteProfileImage(oldStringAvatar);
                    }

                    @Override
                    public void onFailure() {
                        Log.e("Avatar", "Failed to update avatar, so not deleting old image");
                    }
                });
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {

            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {

            }
        });
    }
}
