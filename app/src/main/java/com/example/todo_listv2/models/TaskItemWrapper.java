package com.example.todo_listv2.models;

import com.example.todo_listv2.adapters.ListItemTask;

public class TaskItemWrapper implements ListItemTask {
    private Task task;
    public TaskItemWrapper(Task task){
        this.task = task;
    }
    public Task getTask(){
        return task;
    }

    @Override
    public int getItemType(){
        return task.isProgressTask() ? 2 : 1;
    }
}
