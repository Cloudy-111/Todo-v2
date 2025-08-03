package com.example.todo_listv2.adapters;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;

import java.util.List;

public class ColorBackgroundAdapter extends RecyclerView.Adapter<ColorBackgroundAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(int colorOrDrawable);
    }

    private final List<Integer> items;
    private final boolean isColorMode;
    private final OnItemClickListener listener;

    public ColorBackgroundAdapter(List<Integer> items, boolean isColorMode, OnItemClickListener listener) {
        this.items = items;
        this.isColorMode = isColorMode;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_circle_selector, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int item = items.get(position);
        if (isColorMode) {
            holder.imageView.setImageDrawable(null); // clear image
            GradientDrawable circle = new GradientDrawable();
            circle.setShape(GradientDrawable.OVAL);
            circle.setColor(item);
            holder.imageView.setBackground(circle);
        } else {
            holder.imageView.setBackground(null);
            Drawable drawable = ContextCompat.getDrawable(holder.itemView.getContext(), item);
            holder.imageView.setImageDrawable(drawable);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
