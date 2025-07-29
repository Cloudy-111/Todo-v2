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
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.AlphaSlideBar;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;
import com.skydoves.colorpickerview.ColorEnvelope;

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

        ColorPickerView colorPickerView = new ColorPickerView(context);
        colorPickerView.setInitialColor(initialColorValue);

        new ColorPickerDialog.Builder(context)
                .setTitle("Chọn màu")
                .setView(colorPickerView)
                .setPositiveButton("Chọn", new ColorEnvelopeListener() {
                    @Override
                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                        listener.onPicked(envelope.getColor());
                        Toast.makeText(context, "Đã chọn màu", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss())
                .attachAlphaSlideBar(true)
                .attachBrightnessSlideBar(true)
                .setBottomSpace(12)
                .show();
    }
}
