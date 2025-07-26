package com.example.todo_listv2.adapters;

import androidx.recyclerview.widget.DiffUtil;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class TaskDiffCallback extends DiffUtil.Callback {
    private final List<ListItemTask> oldList;
    private final List<ListItemTask> newList;

    public TaskDiffCallback(List<ListItemTask> oldList, List<ListItemTask> newList){
        this.oldList = oldList != null ? oldList : Collections.emptyList();
        this.newList = newList != null ? newList : Collections.emptyList();
    }

    @Override
    public int getOldListSize() { return oldList.size(); }

    @Override
    public int getNewListSize() { return newList.size(); }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition){
        ListItemTask o = oldList.get(oldItemPosition);
        ListItemTask n = newList.get(newItemPosition);
        return o.getStableId().equals(n.getStableId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ListItemTask o = oldList.get(oldItemPosition);
        ListItemTask n = newList.get(newItemPosition);
        // Nếu các model đã override equals() thì có thể dùng equals()
        return o.equals(n);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Có thể trả về payload tinh hơn để partial bind; tạm thời return null
        return null;
    }
}
