package com.example.todo_listv2.repositories;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.cloudinary.Url;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.UploadPolicy;
import com.cloudinary.utils.ObjectUtils;
import com.example.todo_listv2.BuildConfig;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ImageRepository {
    private static boolean isInitialized = false;
    private static final String TAG = "CloudinaryManager";

    public static void initCloudinary(Context context){
        if(!isInitialized){
            Map<String, String> config = new HashMap<>();

            config.put("cloud_name", BuildConfig.CLOUD_NAME);
            config.put("api_key", BuildConfig.API_KEY);
            config.put("api_secret", BuildConfig.API_SECRET);

            MediaManager.init(context.getApplicationContext(), config);
            isInitialized = true;
        }
    }

    public static void uploadImage(Context context, Uri fileUri, UploadCallback callback){
        initCloudinary(context);

        UploadPolicy uploadPolicy = new UploadPolicy.Builder()
                .networkPolicy(UploadPolicy.NetworkType.ANY)
                .maxRetries(2)
                .build();

        MediaManager.get().upload(fileUri)
                .option("folder", "Todo")
                .option("resource_type", "image")
                .policy(uploadPolicy)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Log.d(TAG, "Upload started");
                        if (callback != null) callback.onStart(requestId);
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        if (callback != null) callback.onProgress(requestId, bytes, totalBytes);
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        Log.d(TAG, "Upload success: " + resultData.get("secure_url"));
                        if (callback != null) callback.onSuccess(requestId, resultData);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.e(TAG, "Upload error: " + error.getDescription());
                        if (callback != null) callback.onError(requestId, error);
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        if (callback != null) callback.onReschedule(requestId, error);
                    }
                })
                .dispatch();
    }

    public static void deleteProfileImage(String oldAvatar){
        String publicId = extractPublicIdFromURL(oldAvatar);
        if (publicId == null) {
            Log.e("CloudinaryManager", "Could not extract public_id from URL");
            return;
        }

        try{
            MediaManager.get().getCloudinary().uploader().destroy(publicId, ObjectUtils.emptyMap());
            Log.d("CloudinaryManager", "Deleted image with public_id: " + publicId);
        } catch (Exception e){
            e.printStackTrace();
            Log.e("CloudinaryManager", "Error When destroy Image");
        }

    }

    private static String extractPublicIdFromURL(String url){
        try{
            URI uri = new URI(url);
            String path = uri.getPath();
            String[] parts = path.split("/");

            StringBuilder publicId = new StringBuilder();
            for(int i = 5; i < parts.length; i++){
                publicId.append(parts[i]);
                if (i < parts.length - 1) publicId.append("/");
            }

            int dotIndex = publicId.lastIndexOf(".");
            if (dotIndex != -1) {
                publicId.setLength(dotIndex);
            }

            return publicId.toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }
}
