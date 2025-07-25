package com.example.todo_listv2.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.adapters.TaskAdapter;
import com.example.todo_listv2.databinding.FragmentDetailTagBinding;
import com.example.todo_listv2.viewModels.TagDetailViewModel;

import java.util.ArrayList;

public class DetailTagFragment extends DialogFragment {
    private FragmentDetailTagBinding binding;
    private SharedPreferences preferences;
    private String taskId, tagId, userId;
    private ImageButton backButton, editButton, filterButton;
    private View allTaskButton, overdueTaskButton, completedTaskButton, willDoTaskButton;
    private View tagColorView;
    private TextView tagName;
    private EditText searchText;
    private RecyclerView listTaskRecycler;
    private TagDetailViewModel tagDetailViewModel;
    private TaskAdapter taskAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentDetailTagBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog);

        preferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        userId = preferences.getString("user_id", "1");
        tagDetailViewModel = new ViewModelProvider(this).get(TagDetailViewModel.class);
        tagId = getArguments() != null ? getArguments().getString("tagId") : null;

        initViews();

        observeData();
        setUpRecycler();

        if(tagId != null){
            tagDetailViewModel.loadData(tagId, userId);
        }

        setListenerOnclick();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
        }
    }

    private void initViews(){
        backButton = binding.backButton;
        editButton = binding.editButton;
        filterButton = binding.filterButton;

        allTaskButton = binding.allTaskFilter;
        overdueTaskButton = binding.overdueTaskFilter;
        completedTaskButton = binding.completedTaskFilter;
        willDoTaskButton = binding.willDoTaskFilter;

        tagColorView = binding.tagColorView;
        tagName = binding.tvTagName;

        listTaskRecycler = binding.listTask;
    }

    private void observeData(){
        tagDetailViewModel.tag.observe(getViewLifecycleOwner(), tag -> {
            if(tag != null){
                tagName.setText(tag.getName());
                int color = Color.parseColor(tag.getColor());
                tagColorView.setBackgroundTintList(ColorStateList.valueOf(color));
            }
        });

        tagDetailViewModel.listItemTask.observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.setTasks(tasks);
        });
    }

    private void setUpRecycler(){
        taskAdapter = new TaskAdapter(new ArrayList<>(), new ArrayList<>(), TaskAdapter.MODE_TODAY_TASK, taskId -> {

        }, tagId -> {

        });
        listTaskRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        listTaskRecycler.setAdapter(taskAdapter);
    }

    private void setListenerOnclick(){
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
        });

        editButton.setOnClickListener(v -> {

        });

        filterButton.setOnClickListener(v -> {

        });

        allTaskButton.setOnClickListener(v -> {

        });

        overdueTaskButton.setOnClickListener(v -> {

        });

        completedTaskButton.setOnClickListener(v -> {

        });

        willDoTaskButton.setOnClickListener(v -> {

        });
    }

}
