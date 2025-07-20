package com.example.todo_listv2.adapters;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.viewHolders.TagChooseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagChooseViewHolder> {
    private List<Tag> tagList;
    private OnTagClickListener listener;

    public interface OnTagClickListener {
        void onTagClick(Tag tag);
    }

    public TagAdapter(List<Tag> tagList, OnTagClickListener listener) {
        this.tagList = tagList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TagChooseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_select, parent, false);
        return new TagChooseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagChooseViewHolder viewHolder, int position){
        Tag tag = tagList.get(position);

        viewHolder.tagNameSelect.setText(tag.getName());
        try {
            int color = Color.parseColor(tag.getColor());
            viewHolder.tagColorView.setBackgroundTintList(ColorStateList.valueOf(color));
        } catch (Exception e) {
            viewHolder.tagColorView.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        viewHolder.itemView.setOnClickListener(v -> listener.onTagClick(tag));
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public void updateData(List<Tag> tagList){
        this.tagList = tagList != null ? tagList : new ArrayList<>();
        notifyDataSetChanged();
    }
}
