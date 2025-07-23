package com.example.todo_listv2.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.google.android.material.checkbox.MaterialCheckBox;

public class CheckListItemDisplayViewHolder extends RecyclerView.ViewHolder {
    public MaterialCheckBox checkBox;
    public TextView contentChecklist;

    public CheckListItemDisplayViewHolder(@NonNull View itemView){
        super(itemView);
        this.checkBox = itemView.findViewById(R.id.checkbox_for_item_checklist);
        this.contentChecklist = itemView.findViewById(R.id.name_checklist_item);
    }
}
