package com.example.todo_listv2.adapters;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.models.Checklist;
import com.example.todo_listv2.viewHolders.CheckListItemDisplayViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CheckListItemDisplayAdapter extends RecyclerView.Adapter<CheckListItemDisplayViewHolder> {
    public List<Checklist> checklistList = new ArrayList<>();
    public onCheckItemListener listener;

    public interface onCheckItemListener{
        void onItemChecked(Checklist item, boolean isChecked);
    }

    public CheckListItemDisplayAdapter(List<Checklist> list, onCheckItemListener listener){
        this.checklistList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CheckListItemDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checklist_display, parent, false);
        return new CheckListItemDisplayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListItemDisplayViewHolder viewHolder, int position){
        Checklist item = checklistList.get(position);

        viewHolder.checkBox.setOnCheckedChangeListener(null);
        viewHolder.checkBox.setChecked(item.isCompleted());
        viewHolder.contentChecklist.setText(item.getContent());

        if(item.isCompleted()){
            viewHolder.contentChecklist.setPaintFlags(viewHolder.contentChecklist.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.contentChecklist.setTextColor(Color.GRAY);
        }

        viewHolder.checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked) {
                listener.onItemChecked(item, true);
            } else {
                if(listener != null){
                    listener.onItemChecked(item, false);
                }
            }
        }));
    }

    @Override
    public int getItemCount(){ return this.checklistList.size(); }

    public void updateData(List<Checklist> list){
        this.checklistList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }
}
