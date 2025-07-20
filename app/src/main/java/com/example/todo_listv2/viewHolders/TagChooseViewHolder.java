package com.example.todo_listv2.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;

public class TagChooseViewHolder extends RecyclerView.ViewHolder {
    public View tagColorView;
    public TextView tagNameSelect;

    public TagChooseViewHolder(View itemView){
        super(itemView);
        tagNameSelect = itemView.findViewById(R.id.tag_name_select);
        tagColorView = itemView.findViewById(R.id.tag_color_preview);
    }
}
