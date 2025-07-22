package com.example.todo_listv2.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.todo_listv2.fake.fakeDB;
import com.example.todo_listv2.models.Tag;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TagRepository {
    private OkHttpClient client = new OkHttpClient();
    private final String baseURL = "http://192.168.10.104:5000";

    public interface TagCallback{
        void onSuccess(String message, Tag tag);
        void onError(String errorMessage);
    }
    public List<Tag> getAllTagByUserId(String userId){
        Request request = new Request.Builder()
                .url(baseURL + "/tag/" + userId)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        List<Tag> result = new ArrayList<>();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resStr = response.body().string();
                JSONArray resJSON = new JSONArray(resStr);
                for (int i = 0; i < resJSON.length(); i++) {
                    JSONObject objects = resJSON.getJSONObject(i);
                    Tag item = new Tag(
                            objects.getString("id"),
                            objects.getString("user_id"),
                            objects.getString("color"),
                            objects.getString("name")
                    );
                    result.add(item);
                }
            }
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    public void saveNewTag(Tag newTag, TagCallback callback){

        Gson gson = new Gson();
        String json;
        try {
            json = gson.toJson(newTag);
        } catch (Exception e){
            e.printStackTrace();
            callback.onError("Error Create JSON");
            return;
        }

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(baseURL + "/tag/create")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Can't connect to server");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Đọc nội dung JSON string
                        String resStr = response.body().string();
                        Log.d("HTTP_RESPONSE", "Raw response: " + resStr);

                        // Parse JSON
                        JSONObject resJSON = new JSONObject(resStr);
                        Log.d("JSON", resJSON.toString());

                        // Xử lý dữ liệu
                        boolean success = resJSON.getBoolean("success");
                        if (success){
                            JSONObject data = resJSON.getJSONObject("data");
                            String id = data.getString("id");
                            String name = data.getString("name");
                            String color = data.getString("color");
                            String userId = data.getString("userId");

                            Tag newTag = new Tag(id, userId, color, name);
                            String message = resJSON.getString("message");
                            callback.onSuccess(message + " " + id, newTag);
                        } else {
                            callback.onError(resJSON.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onError("Error Response: " + e.getMessage());
                    }
                } else {
                    callback.onError("Error Server: " + response.code());
                }
            }
        });
    }


    public Tag getTagById(String tagId){
        Request request = new Request.Builder()
                .url(baseURL + "/tag/getByTagId/" + tagId)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        Tag result = new Tag();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resStr = response.body().string();
                JSONObject json = new JSONObject(resStr);
                result = new Tag(json.getString("id"),
                        json.getString("user_id"),
                        json.getString("color"),
                        json.getString("name"));
            }
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return result;
    }
}
