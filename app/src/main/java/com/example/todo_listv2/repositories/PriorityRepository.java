package com.example.todo_listv2.repositories;

import com.example.todo_listv2.fake.fakeDB;
import com.example.todo_listv2.models.Priority;
import com.example.todo_listv2.models.Tag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PriorityRepository {
    private OkHttpClient client = new OkHttpClient();
    private final String baseURL = "http://192.168.10.104:5000";

    public List<Priority> getAllPriority(){
        Request request = new Request.Builder()
                .url(baseURL + "/priority")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        List<Priority> result = new ArrayList<>();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resStr = response.body().string();
                JSONArray resJSON = new JSONArray(resStr);
                for (int i = 0; i < resJSON.length(); i++) {
                    JSONObject objects = resJSON.getJSONObject(i);
                    Priority item = new Priority(
                            objects.getString("id"),
                            objects.getString("name"),
                            objects.getString("color"),
                            objects.getInt("level")
                    );
                    result.add(item);
                }
            }
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return result;


    }

    public Priority getPriorityById(String priorityId){
        Request request = new Request.Builder()
                .url(baseURL + "/priority/" + priorityId)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        Priority result = new Priority();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String resStr = response.body().string();
                JSONObject json = new JSONObject(resStr);
                result = new Priority(json.getString("id"),
                        json.getString("name"),
                        json.getString("color"),
                        json.getInt("level"));
            }
        } catch (IOException | JSONException e){
            e.printStackTrace();
        }
        return result;
    }
}
