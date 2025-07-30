package com.example.todo_listv2.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.todo_listv2.fake.fakeDB;
import com.example.todo_listv2.models.Note;
import com.example.todo_listv2.models.Tag;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
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
        return fakeDB.getAllNote(userId);
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

    }

    public void updateNote(Note note){

    }

//    public Note loadNote(String noteId){
//
//    }
}
