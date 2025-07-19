package com.example.todo_listv2.viewHolders;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.Utils.ChangeColorUtils;
import com.example.todo_listv2.Utils.DateTimeUtils;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.models.TaskItemWrapper;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.Map;

public class TaskProgressViewHolder extends RecyclerView.ViewHolder {
    public MaterialCheckBox checkBox;
    public TextView nameTask, timeRemind;
    public View colorBar;
    public View progressBar;
    public View progressContainer;

    public TaskProgressViewHolder(View itemView){
        super(itemView);
        checkBox = itemView.findViewById(R.id.check_task_progress);
        nameTask = itemView.findViewById(R.id.name_task_progress);
        timeRemind = itemView.findViewById(R.id.time_task_progress);
        colorBar = itemView.findViewById(R.id.color_bar_progress);
        progressBar = itemView.findViewById(R.id.progress_bar);
        progressContainer = itemView.findViewById(R.id.progress_container);
    }

    public void bind(TaskItemWrapper item, Map<String, Tag> mapTag){
        Task task = item.getTask();
        int maxLengthText = 30;
        checkBox.setChecked(task.isCompleted());

        String titleText = task.getTitle();
        if(titleText.length() > maxLengthText){
            titleText = titleText.substring(0, maxLengthText - 3) + "...";
        }
        nameTask.setText(titleText);
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

        drawProgress(task.getSuccessRate());
    }

    private void drawProgress(double successRate){
        progressContainer.post(() -> {
            int totalWidth = progressContainer.getWidth();
            int progressWidth = (int) (successRate * totalWidth);

            ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
            layoutParams.width = progressWidth;
            progressBar.setLayoutParams(layoutParams);
        });
    }
}
