package com.example.todo_listv2.Utils;

public class TextUtils {
    public static String ellipsize(String originalText, int maxLengthText){
        if(originalText.length() > maxLengthText){
            originalText = originalText.substring(0, maxLengthText - 3) + "...";
        }
        return originalText;
    }
}
