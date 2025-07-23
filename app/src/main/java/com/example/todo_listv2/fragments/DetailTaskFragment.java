package com.example.todo_listv2.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.Utils.DateTimeUtils;
import com.example.todo_listv2.adapters.CheckListItemDisplayAdapter;
import com.example.todo_listv2.databinding.FragmentDetailTaskBinding;
import com.example.todo_listv2.models.Checklist;
import com.example.todo_listv2.viewModels.TaskDetailViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailTaskFragment extends DialogFragment {
    private FragmentDetailTaskBinding binding;
    private SharedPreferences preferences;
    private String userId;
    private ImageButton closeButton, editButton;
    private Button completedTaskButton;
    private TextView nameTask, nameTag, namePriority, noteTask, statusTask, startDateTask, endDateTask, remindAtTask;
    private TaskDetailViewModel taskDetailViewModel;
    private View tagColorView, priorityColorView;
    private RecyclerView checklistItemsRecyclerView;
    private View progressGroup;
    private CheckListItemDisplayAdapter adapter;
    private Map<String, Boolean> initialMapItemStatus = new HashMap<>();
    private Map<String, Boolean> currentMapItemStatus = new HashMap<>();
    public DetailTaskFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentDetailTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog);

        preferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        userId = preferences.getString("user_id", "1");
        taskDetailViewModel = new ViewModelProvider(this).get(TaskDetailViewModel.class);
        String taskId = getArguments() != null ? getArguments().getString("taskId") : null;

        initViews();
        observeData();

        setUpChecklistRecycler();
        if(taskId != null){
            taskDetailViewModel.loadData(taskId);
        }

        listenerOnclick();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        List<String> listItemNeedUpdate = new ArrayList<>();
        for(Map.Entry<String, Boolean> entry : currentMapItemStatus.entrySet()){
            String itemId = entry.getKey();
            Boolean newStatus = entry.getValue();

            Boolean originalStatus = initialMapItemStatus.get(itemId);
            if(originalStatus != null && originalStatus != newStatus){
                listItemNeedUpdate.add(itemId);
            }
        }

        if(!listItemNeedUpdate.isEmpty()){
            taskDetailViewModel.updateChecklistItem(listItemNeedUpdate);
        }

        initialMapItemStatus.clear();
        currentMapItemStatus.clear();
    }

    private void initViews(){
        closeButton = binding.closeDetailTaskButton;
        editButton = binding.editTaskButton;
        completedTaskButton = binding.completeTaskButton;

        nameTask = binding.nameTask;
        nameTag = binding.tagName;
        namePriority = binding.priorityName;
        noteTask = binding.noteText;
        statusTask = binding.statusTextView;
        startDateTask = binding.startDateTextView;
        endDateTask = binding.endDateTextView;
        remindAtTask = binding.remindTextView;

        tagColorView = binding.tagColorView;
        priorityColorView = binding.priorityColorView;

        checklistItemsRecyclerView = binding.checklistItems;
    }

    private void observeData(){
        taskDetailViewModel.taskLiveData.observe(getViewLifecycleOwner(), task -> {
            if(task != null){
                nameTask.setText(task.getTitle());
                noteTask.setText(task.getNote());
                statusTask.setText(task.isCompleted() ? "Completed" : "Not Completed");
                startDateTask.setText(DateTimeUtils.convertMillisToDateString(task.getStartTime()));
                endDateTask.setText(DateTimeUtils.convertMillisToDateString(task.getEndTime()));
                remindAtTask.setText(DateTimeUtils.convertMillisToTimeString(task.getRemindAt()));
            }
        });

        taskDetailViewModel.tagLiveData.observe(getViewLifecycleOwner(), tag -> {
            if(tag != null){
                nameTag.setText(tag.getName());
                int color = Color.parseColor(tag.getColor());
                tagColorView.setBackgroundTintList(ColorStateList.valueOf(color));
            }
        });

        taskDetailViewModel.priorityLiveData.observe(getViewLifecycleOwner(), priority -> {
            if(priority != null){
                namePriority.setText(priority.getName());
                int color = Color.parseColor(priority.getColorHex());
                priorityColorView.setBackgroundTintList(ColorStateList.valueOf(color));
            }
        });

        taskDetailViewModel.checklistItems.observe(getViewLifecycleOwner(), checklists -> {
            adapter.updateData(checklists);
            for(Checklist item : checklists){
                initialMapItemStatus.put(item.getId(), item.isCompleted());
                currentMapItemStatus.put(item.getId(), item.isCompleted());
            }
        });
    }

    private void setUpChecklistRecycler(){
        adapter = new CheckListItemDisplayAdapter(new ArrayList<>(), (item, isChecked) -> {
            item.setCompleted(isChecked);
            currentMapItemStatus.put(item.getId(), isChecked);
        });
        checklistItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        checklistItemsRecyclerView.setAdapter(adapter);
    }

    private void listenerOnclick(){
        closeButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
        });

        editButton.setOnClickListener(v -> {

        });

        completedTaskButton.setOnClickListener(v -> {

        });
    }
}
