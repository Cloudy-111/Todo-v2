package com.example.todo_listv2.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.Utils.DateTimeUtils;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.models.TaskItemWrapper;

public class TaskCommonViewHolder extends RecyclerView.ViewHolder {
//    private View priorityColorView;
    private TextView taskName;
    private TextView taskTime;

    public TaskCommonViewHolder(View itemView){
        super(itemView);
//        this.priorityColorView = itemView.findViewById(R.id.priority_color_view);
        this.taskName = itemView.findViewById(R.id.name_task);
        this.taskTime = itemView.findViewById(R.id.time_task);
    }

    public void bind(TaskItemWrapper item){
        Task task = item.getTask();

        taskName.setText(task.getTitle());
        taskTime.setText(DateTimeUtils.convertMillisToTimeString(task.getRemindAt()));
    }
}
