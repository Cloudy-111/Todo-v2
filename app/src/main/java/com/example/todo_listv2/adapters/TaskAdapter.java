package com.example.todo_listv2.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.models.Priority;
import com.example.todo_listv2.models.PriorityHeaderItem;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.TagHeaderItem;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.models.TaskCommon;
import com.example.todo_listv2.models.TaskItemWrapper;
import com.example.todo_listv2.viewHolders.HeaderViewHolder;
import com.example.todo_listv2.viewHolders.TagHeaderViewHolder;
import com.example.todo_listv2.viewHolders.TaskCommonViewHolder;
import com.example.todo_listv2.viewHolders.TaskProgressViewHolder;
import com.example.todo_listv2.viewHolders.TaskViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int MODE_TODAY_TASK = 1;
    public static final int MODE_ALL_TASK = 2;
    private static final int HEADER_TYPE = 0;
    private static final int TASK_TYPE = 1;
    private static final int TASK_PROGRESS_TYPE = 2;
    private static final int TAG_HEADER_TYPE = 10;
    private static final int TASK_COMMON_TYPE = 3;
    private List<ListItemTask> listItemTasks;
    private Map<String, Tag> mapTag;
    private OnClickTaskItem itemListener;
    private OnClickTagHeader tagHeaderListener;
    private int modeDisplayTask;

    public interface OnClickTaskItem{
        void onTaskItemClicked(String taskId);
    }

    public interface OnClickTagHeader{
        void onTagHeaderClicked(String tagId);
    }

    public TaskAdapter(List<ListItemTask> listItemTasks, List<Tag> listTag, int modeDisplayTask, OnClickTaskItem listener, OnClickTagHeader tagHeaderListener){
        this.listItemTasks = listItemTasks;
        this.modeDisplayTask = modeDisplayTask;
        mapTag = new HashMap<>();
        if (listTag != null) {
            for (Tag tag : listTag) {
                mapTag.put(tag.getId(), tag);
            }
        }

        this.tagHeaderListener = tagHeaderListener;
        this.itemListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if(viewType == HEADER_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_priority_header, parent, false);
            return new HeaderViewHolder(view);
        } else if(viewType == TASK_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_main, parent, false);
            return new TaskViewHolder(view);
        } else if(viewType == TASK_PROGRESS_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_progress, parent, false);
            return new TaskProgressViewHolder(view);
        } else if(viewType == TAG_HEADER_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_header, parent, false);
            return new TagHeaderViewHolder((view));
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_common, parent, false);
            return new TaskCommonViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        ListItemTask item = listItemTasks.get(position);
        int viewType = holder.getItemViewType();

        if(viewType == HEADER_TYPE){
            ((HeaderViewHolder) holder).bind((PriorityHeaderItem) item);
        } else if(viewType == TASK_TYPE){
            ((TaskViewHolder) holder).bind((TaskItemWrapper) item, mapTag);
        } else if(viewType == TASK_PROGRESS_TYPE){
            ((TaskProgressViewHolder) holder).bind((TaskItemWrapper) item, mapTag);
        } else if(viewType == TAG_HEADER_TYPE){
            ((TagHeaderViewHolder) holder).bind((TagHeaderItem) item);
        } else if(viewType == TASK_COMMON_TYPE){
            ((TaskCommonViewHolder) holder).bind((TaskItemWrapper) item);
        }

        if(viewType != HEADER_TYPE && viewType != TAG_HEADER_TYPE){
            holder.itemView.setOnClickListener(v -> {
                String taskId = ((TaskItemWrapper) item).getTask().getId();
                itemListener.onTaskItemClicked(taskId);
            });
        }

        if(viewType == TAG_HEADER_TYPE){
            holder.itemView.setOnClickListener(v -> {
                String tagId = ((TagHeaderItem) item).getTag().getId();
                tagHeaderListener.onTagHeaderClicked(tagId);
            });
        }
    }

    @Override
    public int getItemViewType(int position){
        ListItemTask item = listItemTasks.get(position);

        if (item instanceof TaskItemWrapper) {
            if (modeDisplayTask == MODE_ALL_TASK) {
                return TASK_COMMON_TYPE;
            } else {
                return item.getItemType(); // 1 hoặc 2
            }
        }
        return item.getItemType();
    }

    @Override
    public int getItemCount(){
        return listItemTasks.size();
    }

    public void setTags(Map<String, Tag> tagMap){
        this.mapTag = tagMap != null ? tagMap : new HashMap<>();
        notifyDataSetChanged();
    }

    public void setTasks(List<ListItemTask> tasks){
        this.listItemTasks = tasks != null ? tasks : new ArrayList<>();
        notifyDataSetChanged();
    }
}
