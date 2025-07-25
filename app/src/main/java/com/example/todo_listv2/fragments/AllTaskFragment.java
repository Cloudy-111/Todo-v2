package com.example.todo_listv2.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.todo_listv2.Utils.DateTimeUtils;
import com.example.todo_listv2.adapters.TaskAdapter;
import com.example.todo_listv2.databinding.FragmentAlltaskBinding;
import com.example.todo_listv2.viewModels.TaskDayViewModel;

import java.util.ArrayList;

public class AllTaskFragment extends Fragment {
    private FragmentAlltaskBinding binding;
    private SharedPreferences preferences;
    private TaskDayViewModel taskDayViewModel;
    private EditText searchGlobal;
    private TaskAdapter taskAdapter;
    private String user_id;
    public AllTaskFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentAlltaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        user_id = preferences.getString("user_id", "1");
        taskDayViewModel = new ViewModelProvider(this).get(TaskDayViewModel.class);

        searchGlobal = binding.searchGlobal;

        observerData();
        setRecyclerTaskView();

        taskDayViewModel.loadDataByTag(user_id);
    }

    public void observerData(){
        taskDayViewModel.listItemTasks.observe(getViewLifecycleOwner(), listItem -> {
            taskAdapter.setTasks(listItem);
        });

        taskDayViewModel.tagMap.observe(getViewLifecycleOwner(), tagMap -> {
            taskAdapter.setTags(tagMap);
        });
    }

    public void setRecyclerTaskView(){
        taskAdapter = new TaskAdapter(new ArrayList<>(), new ArrayList<>(), TaskAdapter.MODE_ALL_TASK, taskId -> {

        }, tagId -> {
            DetailTagFragment fragment = new DetailTagFragment();

            Bundle args = new Bundle();
            args.putString("tagId", tagId);
            fragment.setArguments(args);

            fragment.show(requireActivity().getSupportFragmentManager(), "FullScreenDialog");
        });
        binding.recyclerViewTask.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewTask.setAdapter(taskAdapter);
    }
}
