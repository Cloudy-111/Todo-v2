package com.example.todo_listv2.Utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

public class ChangeColorUtils {
    public static void fallbackGray(View view) {
        Drawable background = view.getBackground();
        if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(Color.GRAY);
        }
    }
}
