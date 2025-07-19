package com.example.todo_listv2.repositories;

import com.example.todo_listv2.fake.fakeDB;
import com.example.todo_listv2.models.Tag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TagRepository {
    private OkHttpClient client = new OkHttpClient();
    private final String baseURL = "http://192.168.10.104:5000";

    public interface TagCallback{
        void onSuccess(Tag newTag);
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
//    public Tag getTagByTaskId(String taskId){
//
//    }
}
