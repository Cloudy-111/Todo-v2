package com.example.todo_listv2.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.todo_listv2.R;
import com.example.todo_listv2.Utils.DateTimeUtils;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.repositories.TagRepository;
import com.example.todo_listv2.repositories.TaskRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TodoRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private List<Task> tasks = new ArrayList<>(), listTaskToday = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();
    private TaskRepository taskRepository = new TaskRepository();
    private TagRepository tagRepository = new TagRepository();

    public TodoRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        // initial load
    }

    @Override
    public void onDataSetChanged() {
        String userId = getUserIdFromSharedPrefs(context);
        String today = DateTimeUtils.getTodayDate();

        tasks = new ArrayList<>();
        listTaskToday = taskRepository.getAllTaskByUserIdAndSelectedDay(today, userId);
        for(Task task : listTaskToday){
            if(task.getPriorityId().equals("1")){
                tasks.add(task);
            }
        }
        tags = tagRepository.getAllTagByUserId(userId);
    }

    private String getUserIdFromSharedPrefs(Context context) {
        return context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getString("user_id", "0");
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position >= tasks.size()) return null;

        Task task = tasks.get(position);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_task_widget);

        views.setTextViewText(R.id.name_task, task.getTitle());
        views.setTextViewText(R.id.end_date_task, DateTimeUtils.convertMillisToDateString(task.getEndTime()));

        String colorHex = getColorHexByTagId(task.getTagId());
        int color = Color.parseColor(colorHex);
        views.setInt(R.id.color_bar, "setBackgroundColor", color);

        return views;
    }

    private String getColorHexByTagId(String tagId) {
        for (Tag tag : tags) {
            if (tag.getId().equals(tagId)) {
                return tag.getColor();
            }
        }
        return "#AAAAAA";
    }

    @Override public int getCount() { return tasks.size(); }
    @Override public RemoteViews getLoadingView() { return null; }
    @Override public int getViewTypeCount() { return 1; }
    @Override public long getItemId(int position) { return position; }
    @Override public boolean hasStableIds() { return true; }
    @Override public void onDestroy() {}
}
