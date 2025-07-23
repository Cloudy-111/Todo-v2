package com.example.todo_listv2.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.todo_listv2.models.Checklist;
import com.example.todo_listv2.models.Tag;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckListRepository {
    private OkHttpClient client = new OkHttpClient();
    private String baseURL = "http://192.168.10.104:5000";

    public interface CheckListCallback{
        void onSuccess(String message);
        void onError(String errorMessage);
    }

    public void insertCheckList(Checklist item, CheckListCallback callback){
        Gson gson = new Gson();
        String json;
        try{
            json = gson.toJson(item);
        } catch (Exception e){
            e.printStackTrace();
            callback.onError("Error Create JSON");
            return;
        }

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(baseURL + "/checklist/create")
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
                            callback.onSuccess(resJSON.getString("message"));
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

    public List<Checklist> getAllChecklistByTaskId(String taskId){
        Request request = new Request.Builder()
                .url(baseURL + "/checklist/getAllByTaskId/" + taskId)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        List<Checklist> result = new ArrayList<>();
        try{
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                String resStr = response.body().string();
                JSONArray resJSON = new JSONArray(resStr);
                for(int i = 0; i < resJSON.length(); i++){
                    JSONObject json = resJSON.getJSONObject(i);
                    Checklist item = new Checklist(
                            json.getString("id"),
                            json.getString("content"),
                            json.getBoolean("isCompleted"),
                            json.getString("taskId")
                    );
                    result.add(item);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void updateChecklistItem(List<String> listItemNeedUpdate, CheckListCallback callback){
        String json = new Gson().toJson(listItemNeedUpdate);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(baseURL + "/checklist/toggleCompleted")
                .put(body)
                .build();

        new Thread(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    callback.onSuccess("Checklist updated successfully");
                } else {
                    callback.onError("Failed with code: " + response.code());
                }
            } catch (Exception e) {
                callback.onError("Exception: " + e.getMessage());
            }
        }).start();
    }
}
