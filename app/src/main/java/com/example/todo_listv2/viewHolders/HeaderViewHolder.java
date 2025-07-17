package com.example.todo_listv2.viewHolders;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.models.PriorityHeaderItem;

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView headerText;

    public HeaderViewHolder(View itemView){
        super(itemView);
        this.headerText = itemView.findViewById(R.id.tvPriorityTitle);
    }

    public void bind(PriorityHeaderItem item){
        headerText.setText(item.getPriority().getName());
        try {
            headerText.setTextColor(Color.parseColor(item.getPriority().getColorHex()));
        } catch (Exception e){
            headerText.setTextColor(Color.BLACK);
        }
    }
}
