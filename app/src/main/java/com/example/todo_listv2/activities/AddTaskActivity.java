package com.example.todo_listv2.activities;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.Utils.DateTimeUtils;
import com.example.todo_listv2.adapters.PriorityAdapter;
import com.example.todo_listv2.adapters.TagAdapter;
import com.example.todo_listv2.databinding.ActivityAddTaskBinding;
import com.example.todo_listv2.models.Priority;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.repositories.TagRepository;
import com.example.todo_listv2.viewModels.AddTaskViewModel;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity {
    private ActivityAddTaskBinding binding;
    private SharedPreferences preferences;
    private AddTaskViewModel addTaskViewModel;
    private TagAdapter tagAdapter;
    private PriorityAdapter priorityAdapter;

    private TextView startDateTextView, endDateTextView, remindHourTextView, tagNameDisplay, priorityName;
    private EditText editNoteText, editTitleText, editTagName;
    private Button saveButton, chooseColor, btnCancel, btnConfirm;
    private View selectColorPreview, selectedColor, priorityView;
    private Tag selectedTag;
    private Priority selectedPriority;
    private List<Tag> tagList;
    private List<Priority> priorities;
    private Calendar startDateCalendar, endDateCalendar;
    private String userId;
    private int selectedColorValue = Color.parseColor("#FF7676");

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = preferences.getString("user_id", "0");

        tagList = new ArrayList<>();
        priorities = new ArrayList<>();

        startDateTextView = binding.startDateTextView;
        endDateTextView = binding.endDateTextView;
        editNoteText = binding.editNoteText;
        editTitleText = binding.editTitleText;
        saveButton = binding.saveButton;
        remindHourTextView = binding.remindTextView;

        addTaskViewModel = new ViewModelProvider(this).get(AddTaskViewModel.class);
        observeData();

        tagNameDisplay = binding.tagName;
        selectedColor = binding.tagView;

        priorityName = binding.priorityName;
        priorityView = binding.priorityView;

        startDateTextView.setOnClickListener(v -> DateTimeUtils.showDatePicker(startDateTextView));
        endDateTextView.setOnClickListener(v -> DateTimeUtils.showDatePicker(endDateTextView));

        View tagContainer = findViewById(R.id.edit_tag_name);
        if (tagContainer == null) {
            tagContainer = findViewById(R.id.tag_name).getParent() instanceof View ?
                    (View) findViewById(R.id.tag_name).getParent() : null;
        }
        if (tagContainer != null) {
            tagContainer.setOnClickListener(v -> showTagSelectorDialog());
        }

        View priorityView = binding.prioritySelect;
        if(priorityView != null){
            priorityView.setOnClickListener(v -> showPrioritySelectorDialog());
        }

        remindHourTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimeUtils.showTimePicker(remindHourTextView);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewTask();
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddTaskActivity.this, MainActivity.class));
                overridePendingTransition(0, R.anim.slide_out_bottom);
                finish();
            }
        });
    }

    private void observeData(){
        addTaskViewModel.tagList.observe(this, tagList -> {
            this.tagList = tagList;
            tagAdapter.updateData(tagList);
        });
        addTaskViewModel.priorityList.observe(this, priorities -> {
            this.priorities = priorities;
            priorityAdapter.updateData(priorities);
        });
    }

    private void showPrioritySelectorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_priority_selector, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_priority_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        priorityAdapter = new PriorityAdapter(priorities, priority-> {
            selectedPriority = priority;

            priorityName.setText(selectedPriority.getName());
            try{
                int color = Color.parseColor(priority.getColorHex());
                priorityView.setBackgroundTintList(ColorStateList.valueOf(color));
            } catch (Exception e){
                priorityView.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            }
            dialog.dismiss();
            Toast.makeText(this, priority.getName(), Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(priorityAdapter);
        addTaskViewModel.loadAllPriorities();

        dialog.show();
    }

    private void showTagSelectorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_tag_selector, null);
        builder.setView(dialogView);

        btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        selectColorPreview = dialogView.findViewById(R.id.selected_color_preview);
        editTagName = dialogView.findViewById(R.id.edit_tag_name);

        AlertDialog dialog = builder.create();

        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler_tag_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tagAdapter = new TagAdapter(tagList, tag -> {
            selectedTag = tag;

            tagNameDisplay.setText(tag.getName());
            try {
                int color = Color.parseColor(tag.getColor());
                selectedColor.setBackgroundTintList(ColorStateList.valueOf(color));
            } catch (Exception e) {
                selectedColor.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            }

            dialog.dismiss();
            Toast.makeText(this, "Đã chọn chủ đề: " + tag.getName(), Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(tagAdapter);
        addTaskViewModel.loadAllTags(userId);

        chooseColor = dialogView.findViewById(R.id.btn_pick_custom_color);
        chooseColor.setOnClickListener(v -> pickCustomColor());

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagName = editTagName.getText().toString().trim();
                if (tagName.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Vui lòng nhập tên chủ đề", Toast.LENGTH_SHORT).show();
                    return;
                }

                String hexColor = String.format("#%06X", (0xFFFFFF & selectedColorValue));
                selectedTag = new Tag(userId, hexColor, tagName);

                tagNameDisplay.setText(tagName);
                selectedColor.setBackgroundTintList(ColorStateList.valueOf(selectedColorValue));

                saveNewTag(selectedTag);

                dialog.dismiss();
                Toast.makeText(getBaseContext(), "Đã chọn chủ đề: " + tagName, Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void saveNewTask(){

    }

    private void saveNewTag(Tag tag){
        addTaskViewModel.saveNewTag(tag, new TagRepository.TagCallback() {
            @Override
            public void onSuccess(String message, Tag newTag) {
                selectedTag = newTag;
                runOnUiThread(() -> {
                    Toast.makeText(AddTaskActivity.this, message, Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                    Toast.makeText(AddTaskActivity.this, errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void pickCustomColor(){
        ContextThemeWrapper themedContext = new ContextThemeWrapper(this, R.style.CustomColorPickerDialog);
        ColorPickerDialogBuilder
                .with(themedContext)
                .setTitle("Pick a Color")
                .initialColor(selectedColorValue)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(color -> {

                })
                .setPositiveButton("Chọn", (colorDialog, color, allColors) -> {
                    selectedColorValue = color;
                    if (selectColorPreview != null) {
                        selectColorPreview.setBackgroundTintList(ColorStateList.valueOf(color));
                    }
                    Toast.makeText(this, "Đã chọn màu", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", (colorDialog, which) -> {
                    // Do nothing
                })
                .build()
                .show();
    }
}
