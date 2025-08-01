package com.example.todo_listv2.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.todo_listv2.fake.fakeDB;
import com.example.todo_listv2.models.Note;
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

public class NoteRepository {
    private static OkHttpClient client = new OkHttpClient();
    private static String baseURL = "http://192.168.10.104:5000";

    public List<Note> loadAllNote(String userId){
        JSONObject json = new JSONObject();
        try{
            json.put("userId", userId);
        } catch (Exception e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(baseURL + "/note/getAll")
                .post(body)
                .build();

        List<Note> result = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resStr = response.body().string();
                JSONArray resJSON = new JSONArray(resStr);
                for (int i = 0; i < resJSON.length(); i++) {
                    JSONObject objects = resJSON.getJSONObject(i);
                    Note item = new Note(
                        objects.getString("id"),
                        objects.getString("title"),
                        objects.getString("content"),
                        objects.getString("color"),
                        objects.getInt("backgroundId"),
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

    public Note loadNote(String noteId){
        Request request = new Request.Builder()
                .url(baseURL + "/note/" + noteId)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        Note result = new Note();
        try{
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                String resStr = response.body().string();
                JSONObject json = new JSONObject(resStr);
                result = new Note(
                    json.getString("id"),
                    json.getString("title"),
                    json.getString("content"),
                    json.getString("color"),
                    json.getInt("backgroundId"),
                    json.getString("userId")
                );

            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void createNewNote(Note newNote){
        Gson gson = new Gson();
        String json;
        try{
            json = gson.toJson(newNote);
        } catch (Exception e){
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(baseURL + "/note/create")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Note_error", "Can't connect to server");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String resStr = response.body().string();

                        JSONObject resJSON = new JSONObject(resStr);

                        boolean success = resJSON.getBoolean("success");
                        if (success){
                            Log.d("Note", "Create Successfully");
                        } else {
                            Log.e("Note", resJSON.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Note_error", e.getMessage());
                    }
                } else {
                    Log.e("Note_error", String.valueOf(response.code()));
                }
            }
        });
    }

    public void deleteNote(String noteId){
        Request request = new Request.Builder()
                .url(baseURL + "/note/delete/" + noteId)
                .delete()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Note_Error", "Can't connect to server");
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
                            Log.d("Note", message);
                        } else {
                            Log.d("Note", resJSON.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Note_Error", "Error Response: " + e.getMessage());
                    }
                } else {
                    Log.e("Note_Error", "Error Server: " + response.code());
                }
            }
        });
    }

    public void updateNote(Note note){
        Gson gson = new Gson();
        String json;
        try{
            json = gson.toJson(note);
        } catch (Exception e){
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(baseURL + "/note/edit")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Note_Error", "Can't connect to server");
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
                            Log.d("Note", message);
                        } else {
                            Log.d("Note", resJSON.getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Note_Error", "Error Response: " + e.getMessage());
                    }
                } else {
                    Log.e("Note_Error", "Error Server: " + response.code());
                }
            }
        });
    }

//    public Note loadNote(String noteId){
//
//    }
}
