package com.example.todo_listv2.Utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.todo_listv2.activities.RemindReceiver;
import com.example.todo_listv2.models.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReminderUtils {
        public static void scheduleTaskReminder(Context context, Task task){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(sdf.parse(DateTimeUtils.convertMillisToDateString(task.getStartTime())));

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(sdf.parse(DateTimeUtils.convertMillisToDateString(task.getEndTime())));

            String[] timeParts = DateTimeUtils.formatTime(task.getRemindAt()).substring(0, 5).split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            Calendar alarmCal = (Calendar) startCal.clone();
            alarmCal.set(Calendar.HOUR_OF_DAY, hour);
            alarmCal.set(Calendar.MINUTE, minute);
            alarmCal.set(Calendar.SECOND, 0);

            while (!alarmCal.after(endCal)) {
                long now = System.currentTimeMillis();
                if (alarmCal.getTimeInMillis() > now && alarmCal.before(endCal)) {
                    setExactAlarm(context, alarmCal.getTimeInMillis(), task.getTitle(), task.getId());
                }
                alarmCal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ReminderUtils", "Error when set remind time");
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private static void setExactAlarm(Context context, long taskTimeMillis, String taskTitle, String taskId){
        Intent intent = new Intent(context, RemindReceiver.class);
        intent.putExtra("task_title", taskTitle);

        int requestCode = taskId.hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode, // unique id
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, taskTimeMillis, pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, taskTimeMillis, pendingIntent);
        }

        Log.d("ReminderUtils", "Alarm set: " + taskId);
    }

    private static void cancelTaskReminder(Context context, String taskId) {
        Intent intent = new Intent(context, RemindReceiver.class);
        int requestCode = taskId.hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.d("ReminderUtils", "Alarm cancelled: " + taskId);
    }
}
