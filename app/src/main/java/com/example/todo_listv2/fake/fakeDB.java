package com.example.todo_listv2.fake;

import com.example.todo_listv2.Utils.DateTimeUtils;
import com.example.todo_listv2.models.Priority;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;

import java.util.ArrayList;
import java.util.List;

public class fakeDB {
    public static List<Tag> getAllTag(String userId){
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag("1", "1", "#F96060", "Work"));
        tagList.add(new Tag("2", "1", "#6074F9", "Habit"));
        tagList.add(new Tag("3", "1", "#313131", "Market"));
        tagList.add(new Tag("4", "3", "#03DAC5", "Most Important"));
        tagList.add(new Tag("5", "2", "#E0BBE4", "Oh Wow!"));
        return tagList;
    }

    public static List<Priority> getAllPriority(){
        List<Priority> pList = new ArrayList<>();
        pList.add(new Priority("1", "High", "#FF4444", 1));
        pList.add(new Priority("2", "High", "#FF4444", 1));
        pList.add(new Priority("3", "High", "#FF4444", 1));
        pList.add(new Priority("4", "High", "#FF4444", 1));
        return pList;
    }

    public static List<Task> getAllTaskByUserIdAndSelectedDay(String day, String userId){
        List<Task> list = new ArrayList<>();
        String startTime = "2025-07-10";
        String endTime = "2025-07-15";
        String remindAt = "09:00";
        list.add(new Task("Completed Routine 1", "Time is Money!", "THIS IS NOTE!", DateTimeUtils.convertDateStringToMillis(startTime), DateTimeUtils.convertDateStringToMillis(endTime), DateTimeUtils.convertTimeStringToMillis(remindAt), "1", "2", false, 0, "1"));
        list.add(new Task("Completed Routine 2", "Time is Money!", "THIS IS NOTE!", DateTimeUtils.convertDateStringToMillis(startTime), DateTimeUtils.convertDateStringToMillis(endTime), DateTimeUtils.convertTimeStringToMillis(remindAt), "1", "2", false, 0, "1"));
        list.add(new Task("Completed Routine 3", "Time is Money!", "THIS IS NOTE!", DateTimeUtils.convertDateStringToMillis(startTime), DateTimeUtils.convertDateStringToMillis(endTime), DateTimeUtils.convertTimeStringToMillis(remindAt), "1", "2", false, 0, "1"));
        list.add(new Task("Completed Routine 4", "Time is Money!", "THIS IS NOTE!", DateTimeUtils.convertDateStringToMillis(startTime), DateTimeUtils.convertDateStringToMillis(endTime), DateTimeUtils.convertTimeStringToMillis(remindAt), "1", "2", false, 0, "1"));
        list.add(new Task("Completed Routine 5", "Time is Money!", "THIS IS NOTE!", DateTimeUtils.convertDateStringToMillis(startTime), DateTimeUtils.convertDateStringToMillis(endTime), DateTimeUtils.convertTimeStringToMillis(remindAt), "1", "2", false, 0, "1"));
        list.add(new Task("Completed Routine 6", "Time is Money!", "THIS IS NOTE!", DateTimeUtils.convertDateStringToMillis(startTime), DateTimeUtils.convertDateStringToMillis(endTime), DateTimeUtils.convertTimeStringToMillis(remindAt), "1", "2", false, 0, "1"));
        list.add(new Task("Completed Routine 7", "Time is Money!", "THIS IS NOTE!", DateTimeUtils.convertDateStringToMillis(startTime), DateTimeUtils.convertDateStringToMillis(endTime), DateTimeUtils.convertTimeStringToMillis(remindAt), "1", "2", false, 0, "1"));
        list.add(new Task("Completed Routine 8", "Time is Money!", "THIS IS NOTE!", DateTimeUtils.convertDateStringToMillis(startTime), DateTimeUtils.convertDateStringToMillis(endTime), DateTimeUtils.convertTimeStringToMillis(remindAt), "1", "2", false, 0, "1"));
        list.add(new Task("Completed Routine 9", "Time is Money!", "THIS IS NOTE!", DateTimeUtils.convertDateStringToMillis(startTime), DateTimeUtils.convertDateStringToMillis(endTime), DateTimeUtils.convertTimeStringToMillis(remindAt), "1", "2", false, 0, "1"));
        list.add(new Task("Completed Routine 10", "Time is Money!", "THIS IS NOTE!", DateTimeUtils.convertDateStringToMillis(startTime), DateTimeUtils.convertDateStringToMillis(endTime), DateTimeUtils.convertTimeStringToMillis(remindAt), "1", "2", false, 0, "1"));
        return list;
    }
}
