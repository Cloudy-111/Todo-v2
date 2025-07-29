package com.example.todo_listv2.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.Utils.DateTimeUtils;
import com.example.todo_listv2.adapters.TaskAdapter;
import com.example.todo_listv2.databinding.FragmentCalendarBinding;
import com.example.todo_listv2.viewModels.TaskDayViewModel;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {
    private FragmentCalendarBinding binding;
    private SharedPreferences preferences;
    private String userId;
    private CalendarView calendarView;
    private TextView textCalendar;
    private RecyclerView taskList;
    private TaskDayViewModel taskDayViewModel;
    private TaskAdapter taskAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = preferences.getString("user_id", "1");
        taskDayViewModel = new ViewModelProvider(this).get(TaskDayViewModel.class);

        initViews();
        observeData();

        textCalendar.setText(DateTimeUtils.getTodayDate());
        setRecyclerTaskView();

        taskDayViewModel.loadData(DateTimeUtils.getTodayDate(), userId);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = String.format("%04d/%02d/%02d", year, month + 1, dayOfMonth);
                textCalendar.setText(date);
                taskDayViewModel.loadData(date, userId);
            }
        });
    }

    private void setRecyclerTaskView(){
        taskAdapter = new TaskAdapter(new ArrayList<>(), new ArrayList<>(), TaskAdapter.MODE_TODAY_TASK, taskId -> {

        }, tagId -> {

        });
        taskList.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskList.setAdapter(taskAdapter);
    }

    private void observeData(){
        taskDayViewModel.listItemTasks.observe(getViewLifecycleOwner(), listItem -> {
            taskAdapter.setTasks(listItem);
        });

        taskDayViewModel.tagMap.observe(getViewLifecycleOwner(), tagMap -> {
            taskAdapter.setTags(tagMap);
        });
    }

    private void initViews(){
        calendarView = binding.calendar;
        textCalendar = binding.dateText;
        taskList = binding.listTask;
    }
}
