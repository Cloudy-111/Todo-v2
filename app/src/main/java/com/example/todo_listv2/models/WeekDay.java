package com.example.todo_listv2.models;

import android.icu.util.LocaleData;

import java.time.LocalDate;
import java.util.Locale;

public class WeekDay {
    private String dayName;
    private String dayText;
    private LocalDate date;

    public WeekDay(String dayName, String dayText, LocalDate date) {
        this.dayName = dayName;
        this.dayText = dayText;
        this.date = date;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getDayText() {
        return dayText;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
