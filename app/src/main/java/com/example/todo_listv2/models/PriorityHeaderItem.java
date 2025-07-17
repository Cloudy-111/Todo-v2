package com.example.todo_listv2.models;

import com.example.todo_listv2.adapters.ListItemTask;

public class PriorityHeaderItem implements ListItemTask {
    private Priority priority;
    public PriorityHeaderItem(Priority priority){
        this.priority = priority;
    }

    public Priority getPriority(){
        return priority;
    }
    @Override
    public int getItemType(){
        return 0;
    }
}
