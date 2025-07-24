package com.example.todo_listv2.models;

import com.example.todo_listv2.adapters.ListItemTask;

public class TaskCommon implements ListItemTask {
    private Task task;
    public TaskCommon(Task task){
        this.task = task;
    }
    public Task getTask(){
        return task;
    }

    @Override
    public int getItemType(){
        return 3;
    }
}
