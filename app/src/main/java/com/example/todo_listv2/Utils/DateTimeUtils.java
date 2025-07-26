package com.example.todo_listv2.Utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.TextView;

import com.example.todo_listv2.models.WeekDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateTimeUtils {
    public static String getTodayDate() {
        return new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
    }

    public static String formatTime(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    public static long convertDateStringToMillis(String dateString, boolean isEndTime){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            Date date = sdf.parse(dateString);
            return isEndTime ? date.getTime() + 86_400_400L - 1 : date.getTime();
        } catch (ParseException e){
            e.printStackTrace();
            return -1;
        }
    }

    public static String convertMillisToDateString(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(millis);
        return sdf.format(date);
    }

    public static String convertMillisToTimeString(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date = new Date(millis);
        return sdf.format(date);
    }

    public static long convertTimeStringToMillis(String timeString){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date date = sdf.parse(timeString);
            return date.getTime();
        } catch (ParseException e){
            e.printStackTrace();
            return -1;
        }
    }

    public static List<WeekDay> getCurrentWeekDays() {
        List<WeekDay> days = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        String[] dayNames = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd", Locale.getDefault());

        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            String dayName = dayNames[i];
            String dateStr = date.format(dayFormatter);
            days.add(new WeekDay(dayName, dateStr, date));
        }

        return days;
    }

    public static void showDatePicker(final TextView targetView) {
        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                targetView.getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%04d/%02d/%02d", selectedYear, selectedMonth + 1, selectedDay);
                    targetView.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    public static void showTimePicker(final TextView targetView) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                targetView.getContext(),
                (view, selectedHour, selectedMinute) -> {
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    targetView.setText(time);
                },
                hour, minute, true
        );

        timePickerDialog.show();
    }
}
