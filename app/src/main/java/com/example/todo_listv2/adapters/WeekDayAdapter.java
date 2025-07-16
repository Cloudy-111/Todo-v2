package com.example.todo_listv2.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.models.WeekDay;
import com.example.todo_listv2.viewHolders.WeekDayViewHolder;

import java.time.LocalDate;
import java.util.List;

public class WeekDayAdapter extends RecyclerView.Adapter<WeekDayViewHolder> {
    private List<WeekDay> weekDays;
    private final OnItemListener onItemListener;
    private final LocalDate selectedDate;
    public WeekDayAdapter(List<WeekDay> weekDays, LocalDate selectedDate, OnItemListener onItemListener) {
        this.weekDays = weekDays;
        this.onItemListener = onItemListener;
        this.selectedDate = selectedDate;
    }

    @NonNull
    @Override
    public WeekDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
        return new WeekDayViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekDayViewHolder holder, int position){
        WeekDay day = weekDays.get(position);
        holder.tvDayName.setText(day.getDayName());
        holder.tvDate.setText(day.getDayText());

        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int paddingScreen = 10;
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.width = (screenWidth - paddingScreen * 2) / weekDays.size();
        holder.itemView.setLayoutParams(params);
    }

    @Override
    public int getItemCount(){ return weekDays.size(); }
    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
