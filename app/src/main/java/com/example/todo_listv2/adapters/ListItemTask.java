package com.example.todo_listv2.adapters;

import androidx.annotation.NonNull;

public interface ListItemTask {
    int getItemType();
    @NonNull String getStableId();
}
