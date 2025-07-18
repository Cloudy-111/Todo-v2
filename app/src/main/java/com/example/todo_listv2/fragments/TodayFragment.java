package com.example.todo_listv2.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.Utils.DateTimeUtils;
import com.example.todo_listv2.adapters.ListItemTask;
import com.example.todo_listv2.adapters.TaskAdapter;
import com.example.todo_listv2.adapters.WeekDayAdapter;
import com.example.todo_listv2.databinding.FragmentTodayBinding;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.WeekDay;
import com.example.todo_listv2.viewHolders.WeekDayViewHolder;
import com.example.todo_listv2.viewModels.TaskDayViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TodayFragment extends Fragment implements WeekDayAdapter.OnItemListener {
    private FragmentTodayBinding binding;
    private RecyclerView weekDaysRecycler;
    private SharedPreferences preferences;
    private LocalDate selectedDate;
    private TextView textToday;
    private List<WeekDay> weekDays;
    private TaskDayViewModel taskDayViewModel;
    private TaskAdapter taskAdapter;
    public TodayFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentTodayBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "1");
        taskDayViewModel = new ViewModelProvider(this).get(TaskDayViewModel.class);

        initCalendar();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd/MM/yyyy", Locale.ENGLISH);
        binding.textToday.setText(selectedDate.format(formatter));

        setWeekView();

        observerData();
        setRecyclerTaskView();

        taskDayViewModel.loadData("2025-07-13", "1");
    }

    private void initCalendar(){
        selectedDate = LocalDate.now();
        weekDaysRecycler = binding.recyclerViewWeekDays;
    }

    private void setWeekView(){
        weekDays = DateTimeUtils.getCurrentWeekDays();
        WeekDayAdapter weekDayAdapter = new WeekDayAdapter(weekDays, selectedDate, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        weekDaysRecycler.setLayoutManager(layoutManager);
        weekDaysRecycler.setAdapter(weekDayAdapter);
    }

    private void observerData(){
        taskDayViewModel.listItemTasks.observe(getViewLifecycleOwner(), listItem -> {
            taskAdapter.setTasks(listItem);
        });

        taskDayViewModel.tagMap.observe(getViewLifecycleOwner(), tagMap -> {
            taskAdapter.setTags(tagMap);
        });
    }

    private void setRecyclerTaskView(){
        taskAdapter = new TaskAdapter(new ArrayList<>(), new ArrayList<>());
        binding.recyclerViewTask.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewTask.setAdapter(taskAdapter);
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if (!dayText.equals("")) {
            selectedDate = weekDays.get(position).getDate();

            setWeekView();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd/MM/yyyy", Locale.ENGLISH);
            String formattedDate = selectedDate.format(formatter);
            binding.textToday.setText(formattedDate);
        }
    }
}
