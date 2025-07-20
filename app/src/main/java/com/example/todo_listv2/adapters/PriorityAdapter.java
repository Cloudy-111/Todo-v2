package com.example.todo_listv2.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.models.Priority;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.viewHolders.PrioritySelectorViewHolder;

import java.util.List;

public class PriorityAdapter extends RecyclerView.Adapter<PrioritySelectorViewHolder> {
    private List<Priority> priorityList;
    private OnPriorityClickListener listener;

    public interface OnPriorityClickListener {
        void onTagClick(Priority priority);
    }

    public PriorityAdapter(List<Priority> priorities, OnPriorityClickListener listener){
        this.priorityList = priorities;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PrioritySelectorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_priority_select, parent, false);
        return new PrioritySelectorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrioritySelectorViewHolder viewHolder, int position){
        Priority priority = priorityList.get(position);

        viewHolder.priorityNameSelect.setText(priority.getName());
        try{
            int color = Color.parseColor(priority.getColorHex());
            viewHolder.priorityColorView.setBackgroundTintList(ColorStateList.valueOf(color));
        } catch (Exception e){
            viewHolder.priorityColorView.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        }

        viewHolder.itemView.setOnClickListener(v -> listener.onTagClick(priority));
    }

    @Override
    public int getItemCount(){
        return priorityList.size();
    }

    public void updateData(List<Priority> priorities){
        this.priorityList = priorities;
        notifyDataSetChanged();
    }
}
