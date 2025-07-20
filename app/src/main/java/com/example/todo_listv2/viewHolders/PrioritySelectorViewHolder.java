package com.example.todo_listv2.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;

public class PrioritySelectorViewHolder extends RecyclerView.ViewHolder {
    public View priorityColorView;
    public TextView priorityNameSelect;
    public PrioritySelectorViewHolder(View itemView){
        super(itemView);
        priorityColorView = itemView.findViewById(R.id.priority_color_view);
        priorityNameSelect = itemView.findViewById(R.id.priority_name_view);
    }
}
