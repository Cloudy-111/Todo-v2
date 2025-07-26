package com.example.todo_listv2.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;

import com.example.todo_listv2.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class ColorUtils {
    public interface OnColorPickedListener {
        void onPicked(@ColorInt int color);
        default void onCanceled() {}
    }
    public static void fallbackGray(View view) {
        Drawable background = view.getBackground();
        if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(Color.GRAY);
        }
    }

    public static void fallbackError(View view) {
        Drawable background = view.getBackground();
        if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(Color.RED);
        }
    }

    public static void showColorPicker(@NonNull Context context, int initialColorValue, @NonNull OnColorPickedListener listener){
        ContextThemeWrapper themedContext = new ContextThemeWrapper(context, R.style.CustomColorPickerDialog);
        ColorPickerDialogBuilder
                .with(themedContext)
                .setTitle("Pick a Color")
                .initialColor(initialColorValue)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(color -> {

                })
                .setPositiveButton("Chọn", (colorDialog, color, allColors) -> {
                    listener.onPicked(color);
                    Toast.makeText(context, "Đã chọn màu", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", (colorDialog, which) -> {
                    // Do nothing
                })
                .build()
                .show();
    }
}
