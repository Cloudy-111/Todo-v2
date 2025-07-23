package com.example.todo_listv2.repositories;

import android.health.connect.datatypes.MealType;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.RequestBuilder;
import com.example.todo_listv2.fake.fakeDB;
import com.example.todo_listv2.models.Checklist;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
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

public class TaskRepository {
    private OkHttpClient client = new OkHttpClient();
    private final String baseURL = "http://192.168.10.104:5000";

    public interface TaskCallback{
        void onSuccess(String taskId);
        void onError(String error);
    }

    public List<Task> getAllTaskByUserIdAndSelectedDay(String day, String userId){
        JSONObject json = new JSONObject();
        try{
            json.put("daySelect", day);
            json.put("userId", userId);
        } catch (Exception e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(baseURL + "/task/getDayTask")
                .post(body)
                .build();

        List<Task> result = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resStr = response.body().string();
                JSONArray resJSON = new JSONArray(resStr);
                for (int i = 0; i < resJSON.length(); i++) {
                    JSONObject objects = resJSON.getJSONObject(i);
                    Task item = new Task(
                            objects.getString("id"),
                            objects.getString("title"),
                            objects.getString("description"),
                            objects.getString("note"),
                            objects.getLong("startTime"),
                            objects.getLong("endTime"),
                            objects.getLong("remindAt"),
                            objects.getBoolean("isCompleted"),
                            objects.getLong("completedAt"),
                            objects.getLong("createAt"),
                            objects.getString("priorityId"),
                            objects.getString("tagId"),
                            objects.getBoolean("isProgressTask"),
                            objects.getDouble("successRate"),
                            objects.getString("userId")
                    );
                    result.add(item);
                }
            }
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    public Task getTaskById(String taskId){
        Request request = new Request.Builder()
                .url(baseURL + "/task/" + taskId)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        Task result = new Task();
        try{
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                String resStr = response.body().string();
                JSONObject json = new JSONObject(resStr);
                result = new Task(
                        json.getString("id"),
                        json.getString("title"),
                        json.getString("description"),
                        json.getString("note"),
                        json.getLong("startTime"),
                        json.getLong("endTime"),
                        json.getLong("remindAt"),
                        json.getBoolean("isCompleted"),
                        json.getLong("completedAt"),
                        json.getLong("createAt"),
                        json.getString("priorityId"),
                        json.getString("tagId"),
                        json.getBoolean("isProgressTask"),
                        json.getDouble("successRate"),
                        json.getString("userId")
                );

                Log.d("TASK", result.toString());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void saveNewTask(Task newTask, TaskCallback callback){
        String json;
        Gson gson = new Gson();
        try{
            json = gson.toJson(newTask);
        } catch (Exception e){
            e.printStackTrace();
            callback.onError("Error Create JSON");
            return;
        }

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(baseURL + "/task/create")
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
                        // Đọc nội dung JSON string`
                        String resStr = response.body().string();
                        Log.d("HTTP_RESPONSE", "Raw response: " + resStr);

                        // Parse JSON
                        JSONObject resJSON = new JSONObject(resStr);
                        Log.d("JSON", resJSON.toString());

                        // Xử lý dữ liệu
                        boolean success = resJSON.getBoolean("success");
                        if (success){
                            String taskId = resJSON.getString("taskId");
                            String message = resJSON.getString("message");
                            callback.onSuccess(taskId);
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

    public void deleteTaskById(String taskId, TaskCallback callback){
        Request request = new Request.Builder()
                .url(baseURL + "/task/delete/" + taskId)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess("Task deleted successfully");
                } else {
                    callback.onError("Failed to delete task");
                }
            }
        });
    }

    public void makeTaskCompleted(String taskId){
        Request request = new Request.Builder()
                .url(baseURL + "/task/completed/" + taskId)
                .put(RequestBody.create("", MediaType.parse("application/json")))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("Task", "Exception: " + (e.getMessage() != null ? e.getMessage() : "Unknown error"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("Task", "Task Completed");
                } else {
                    Log.d("Task", "Failed to complete task");
                }
            }
        });
    }

    public void updateProgressTask(String taskId, double successRate){
        JSONObject json = new JSONObject();
        try {
            json.put("successRate", successRate);

            RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(baseURL + "/task/updateProgress/" + taskId)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
//                    Log.d("Task", "Exception: " + (e.getMessage() != null ? e.getMessage() : "Unknown error"));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
//                        Log.d("Task", "Task Updated");
                    } else {
//                        Log.d("Task", "Failed to complete task");
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
