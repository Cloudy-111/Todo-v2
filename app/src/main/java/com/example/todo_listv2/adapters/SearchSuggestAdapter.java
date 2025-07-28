package com.example.todo_listv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.models.TaskItemWrapper;
import com.example.todo_listv2.viewHolders.SearchSuggestItemViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchSuggestAdapter extends RecyclerView.Adapter<SearchSuggestItemViewHolder> {
    public interface OnItemClickListener{
        void onItemClick(String taskId);
}
    private List<Task> tasks;
    private OnItemClickListener listener;

    public SearchSuggestAdapter(List<Task> tasks, OnItemClickListener listener){
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchSuggestItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_suggest, parent, false);
        return new SearchSuggestItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchSuggestItemViewHolder viewHolder, int position){
        Task task = tasks.get(position);

        viewHolder.searchSuggest.setText(task.getTitle());

        viewHolder.itemView.setOnClickListener(v -> listener.onItemClick(task.getId()));
    }

    @Override
    public int getItemCount(){return tasks.size();}

    public void updateData(List<Task> taskList){
        this.tasks = taskList != null ? taskList : new ArrayList<>();
        notifyDataSetChanged();
    }
}
