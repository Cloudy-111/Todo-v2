package com.example.todo_listv2.repositories;

import android.content.Context;
import android.content.SharedPreferences;

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
}
