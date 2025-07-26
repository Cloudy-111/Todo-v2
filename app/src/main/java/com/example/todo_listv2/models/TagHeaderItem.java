package com.example.todo_listv2.models;

import com.example.todo_listv2.adapters.ListItemTask;

public class TagHeaderItem implements ListItemTask {
    private Tag tag;
    public TagHeaderItem(Tag tag){
        this.tag = tag;
    }
    public Tag getTag(){
        return tag;
    }

    @Override
    public int getItemType(){
        return 10;
    }

    @Override
    public String getStableId(){
        return "tag_header_" + tag.getId();
    }
}
