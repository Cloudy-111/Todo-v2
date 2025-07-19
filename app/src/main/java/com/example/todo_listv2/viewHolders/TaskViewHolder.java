package com.example.todo_listv2.viewHolders;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.Utils.ChangeColorUtils;
import com.example.todo_listv2.Utils.DateTimeUtils;
import com.example.todo_listv2.Utils.TextUtils;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.models.TaskItemWrapper;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.Map;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    private static final int MAX_LENGTH_TITLE = 30;
    public MaterialCheckBox checkBox;
    public TextView nameTask, timeRemind;
    public View colorBar;

    public TaskViewHolder(View itemView){
        super(itemView);
        this.checkBox = itemView.findViewById(R.id.check_task);
        this.nameTask = itemView.findViewById(R.id.name_task);
        this.timeRemind = itemView.findViewById(R.id.time_task);
        this.colorBar = itemView.findViewById(R.id.color_bar);
    }

    public void bind(TaskItemWrapper item, Map<String, Tag> mapTag){
        Task task = item.getTask();
        checkBox.setChecked(task.isCompleted());
        nameTask.setText(TextUtils.ellipsize(task.getTitle(), MAX_LENGTH_TITLE));
        timeRemind.setText(DateTimeUtils.formatTime(task.getRemindAt()));

        Tag tag = mapTag.get(task.getTagId());
        if(tag != null){
            try{
                int color = Color.parseColor(tag.getColor());
                Drawable background = colorBar.getBackground();
                if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(color);
                }
            } catch (IllegalArgumentException e){
                ChangeColorUtils.fallbackGray(colorBar);
            }
        } else {
            ChangeColorUtils.fallbackError(colorBar);
        }
    }
}
