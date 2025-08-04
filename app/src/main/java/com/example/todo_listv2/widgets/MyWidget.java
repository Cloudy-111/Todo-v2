package com.example.todo_listv2.widgets;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.todo_listv2.R;
import com.example.todo_listv2.Utils.DateTimeUtils;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.repositories.TagRepository;
import com.example.todo_listv2.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class MyWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] appWidgetIds){
        for (int id : appWidgetIds) {
            Intent serviceIntent = new Intent(context, WidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);

            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_task);
            views.setRemoteAdapter(R.id.task_list_view, serviceIntent);
            views.setEmptyView(R.id.task_list_view, R.id.empty_view);


            manager.updateAppWidget(id, views);
            manager.notifyAppWidgetViewDataChanged(id, R.id.task_list_view);
            Log.d("Widget", "Updated widget ID: " + id);
        }


    }
}
