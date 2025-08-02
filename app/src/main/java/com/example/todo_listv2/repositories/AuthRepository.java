package com.example.todo_listv2.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.todo_listv2.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthRepository {
    private final OkHttpClient client = new OkHttpClient();
    private final String baseURL = "http://192.168.10.104:5000";
    private SharedPreferences preferences;
    private Context context;

    public interface OnAvatarUpdatedCallback {
        void onSuccess();
        void onFailure();
    }

    public AuthRepository(){}

    public AuthRepository(Context context) {
        this.context = context;
    }

    public interface AuthCallback{
        void onSuccess(String message);
        void onError(String error);
    }

    public void login(String username, String password, AuthCallback callback){
        try{
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);

            RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(baseURL + "/user/login")
                    .post(body)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onError("Network error");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        if (response.isSuccessful()) {
                            try {
                                String res = response.body().string();
                                JSONObject resJSON = new JSONObject(res);
                                String userId = resJSON.getString("user_id");

                                preferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                                preferences.edit().putString("user_id", userId).apply();

                                callback.onSuccess("Success");
                            } catch (JSONException e) {
                                callback.onError("Parse error");
                            }
                        } else {
                            callback.onError("Invalid login");
                        }
                    } else {
                        callback.onError("Invalid username or password");
                    }
                }
            });
        } catch (Exception e){
            callback.onError("Error in Response");
        }
    }

    public void register(User user, AuthCallback callback){
        try{
            JSONObject json = new JSONObject();
            json.put("id", user.getId());
            json.put("username", user.getUsername());
            json.put("email", user.getEmail());
            json.put("password", user.getPassword());
            json.put("avatar", user.getAvatar());

            RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(baseURL + "/user/register")
                    .post(body)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onError("Network Error");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(response.isSuccessful()){
                        callback.onSuccess("Register Successful");
                    } else {
                        callback.onError("Exist Username");
                    }
                }
            });
        } catch (Exception e){
            callback.onError("Error in Response");
        }
    }

    public User loadUser(String userId){
        Request request = new Request.Builder()
                .url(baseURL + "/user/" + userId)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        User result = new User();
        try{
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                String resStr = response.body().string();
                JSONObject json = new JSONObject(resStr);
                JSONObject data = json.getJSONObject("data");
                result = User.loadUser(
                        data.getString("id"),
                        data.getString("username"),
                        data.getString("email"),
                        data.getString("avatar")
                );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void updateProfileImageUrl(String imageUrl, String userId, OnAvatarUpdatedCallback callback){
        JSONObject json = new JSONObject();
        try{
            json.put("userId", userId);
            json.put("avatar", imageUrl);
        } catch (Exception e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(baseURL + "/user/updateAvatar")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Auth_error", "Can't connect to server");
                callback.onFailure();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String resStr = response.body().string();

                        JSONObject resJSON = new JSONObject(resStr);
                        boolean success = resJSON.getBoolean("success");
                        if (success){
                            String message = resJSON.getString("message");
                            Log.d("Auth", message);
                            callback.onSuccess();
                        } else {
                            Log.d("Auth", resJSON.getString("message"));
                            callback.onFailure();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Auth_error", "Error Response: " + e.getMessage());
                        callback.onFailure();
                    }
                } else {
                    Log.e("Auth_error", "Error Server: " + response.code());
                    callback.onFailure();
                }
            }
        });
    }
}
