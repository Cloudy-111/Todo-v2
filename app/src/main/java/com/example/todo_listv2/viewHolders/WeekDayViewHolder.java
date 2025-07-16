package com.example.todo_listv2.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.adapters.WeekDayAdapter;

public class WeekDayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView tvDayName, tvDate;
    private final WeekDayAdapter.OnItemListener onItemListener;
    public WeekDayViewHolder(@NonNull View itemView, WeekDayAdapter.OnItemListener onItemListener){
        super(itemView);
        tvDayName = itemView.findViewById(R.id.tvDayOfWeek);
        tvDate = itemView.findViewById(R.id.tvDate);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View view)
    {
        onItemListener.onItemClick(getAdapterPosition(), (String) tvDate.getText());
    }
}
