package com.example.todo_listv2.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;

public class CheckListItemViewHolder extends RecyclerView.ViewHolder {
    public TextView contentItem;
    public ImageView deleteButton, editButton;

    public CheckListItemViewHolder(@NonNull View itemView){
        super(itemView);
        this.contentItem = itemView.findViewById(R.id.content_check_list_item);
    }
}
