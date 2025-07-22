package com.example.todo_listv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.models.Checklist;
import com.example.todo_listv2.viewHolders.CheckListItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CheckListItemAdapter extends RecyclerView.Adapter<CheckListItemViewHolder> {
    private List<Checklist> checklistList = new ArrayList<>();
    private onDeleteClickListener delListener;
    private onEditClickListener editListener;

    public interface onDeleteClickListener{
        void onDeleteClick(Checklist item);
    }
    public interface onEditClickListener{
        void onEditClick(Checklist item);
    }

    @NonNull
    @Override
    public CheckListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checklist_create, parent, false);
        return new CheckListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListItemViewHolder viewHolder, int position){
        Checklist item = checklistList.get(position);

        viewHolder.contentItem.setText(item.getContent());
    }

    @Override
    public int getItemCount(){ return checklistList.size();};

    public void updateData(List<Checklist> list){
        this.checklistList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }
}
