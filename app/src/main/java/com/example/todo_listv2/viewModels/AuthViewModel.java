package com.example.todo_listv2.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todo_listv2.models.User;
import com.example.todo_listv2.repositories.AuthRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository = new AuthRepository();

    private MutableLiveData<User> _user = new MutableLiveData<>();
    public LiveData<User> user = _user;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void loadDataUser(String userId){
        executor.execute(() -> {
            User result = authRepository.loadUser(userId);

            _user.postValue(result);
        });
    }
}
