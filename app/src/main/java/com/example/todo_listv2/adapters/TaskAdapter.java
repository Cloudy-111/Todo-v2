package com.example.todo_listv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.models.PriorityHeaderItem;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.models.TaskItemWrapper;
import com.example.todo_listv2.viewHolders.HeaderViewHolder;
import com.example.todo_listv2.viewHolders.TaskProgressViewHolder;
import com.example.todo_listv2.viewHolders.TaskViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HEADER_TYPE = 0;
    private static final int TASK_TYPE = 1;
    private static final int TASK_PROGRESS_TYPE = 2;
    private List<ListItemTask> listItemTasks;
    private Map<String, Tag> mapTag;

    public TaskAdapter(List<ListItemTask> listItemTasks){
        this.listItemTasks = listItemTasks;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if(viewType == HEADER_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_priority_header, parent, false);
            return new HeaderViewHolder(view);
        } else if(viewType == TASK_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_main, parent, false);
            return new TaskViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_progress, parent, false);
            return new TaskProgressViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        ListItemTask item = listItemTasks.get(position);

        if(item.getItemType() == HEADER_TYPE){
            ((HeaderViewHolder) holder).bind((PriorityHeaderItem) item);
        } else if(item.getItemType() == TASK_TYPE){
            ((TaskViewHolder) holder).bind((TaskItemWrapper) item, mapTag);
        } else {
            ((TaskProgressViewHolder) holder).bind((TaskItemWrapper) item, mapTag);
        }
    }

    @Override
    public int getItemViewType(int position){
        return listItemTasks.get(position).getItemType();
    }

    @Override
    public int getItemCount(){
        return listItemTasks.size();
    }

    public void setTags(List<Tag> tags){
        mapTag = new HashMap<>();
        for (Tag tag : tags) {
            mapTag.put(tag.getId(), tag);
        }
        notifyDataSetChanged();
    }
}
