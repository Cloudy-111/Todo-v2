package com.example.todo_listv2.adapters;

import androidx.recyclerview.widget.DiffUtil;

import com.example.todo_listv2.models.Note;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class NoteDiffCallback extends DiffUtil.Callback{
    private List<Note> oldList;
    private List<Note> newList;

    public NoteDiffCallback(List<Note> oldList, List<Note> newList){
        this.oldList = oldList != null ? oldList : Collections.emptyList();
        this.newList = newList != null ? newList : Collections.emptyList();
    }

    @Override
    public int getOldListSize() { return oldList.size(); }

    @Override
    public int getNewListSize() { return newList.size(); }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition){
        Note o = oldList.get(oldItemPosition);
        Note n = newList.get(newItemPosition);
        return o.getId().equals(n.getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Note o = oldList.get(oldItemPosition);
        Note n = newList.get(newItemPosition);
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
