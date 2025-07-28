package com.example.todo_listv2.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.adapters.SearchSuggestAdapter;
import com.example.todo_listv2.databinding.FragmentSearchSuggestBinding;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.viewModels.TaskDayViewModel;

import java.util.ArrayList;

public class SearchSuggestFragment extends DialogFragment {
    private FragmentSearchSuggestBinding binding;
    private SharedPreferences preferences;
    private SearchSuggestAdapter adapter;
    private String userId;
    private TaskDayViewModel taskDayViewModel;
    private EditText searchText;
    private RecyclerView suggestList;
    private TaskClickListener listener;
    private TextView notiWhenEmpty;

    // Set delay when search
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    public interface TaskClickListener {
        void onTaskClicked(Task task);
    }

    public SearchSuggestFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentSearchSuggestBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        userId = preferences.getString("user_id", "1");
        taskDayViewModel = new ViewModelProvider(this).get(TaskDayViewModel.class);

        searchText = binding.editTextSearch;
        suggestList = binding.recyclerSuggestions;
        notiWhenEmpty = binding.textViewEmpty;

        observeData();

        adapter = new SearchSuggestAdapter(new ArrayList<>(), taskId -> {
            DetailTaskFragment fragment = new DetailTaskFragment();

            Bundle args = new Bundle();
            args.putString("taskId", taskId);
            fragment.setArguments(args);

            fragment.show(requireActivity().getSupportFragmentManager(), "FullScreenDialog");
        });
        suggestList.setLayoutManager(new LinearLayoutManager(getContext()));
        suggestList.setAdapter(adapter);

        searchText.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {
                    taskDayViewModel.loadTaskSuggest(s.toString(), userId);
                };

                handler.postDelayed(searchRunnable, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        taskDayViewModel.loadTaskSuggest("", userId);
    }

    private void observeData(){
        taskDayViewModel.listTask.observe(getViewLifecycleOwner(), tasks -> {
            adapter.updateData(tasks);
            if (tasks == null || tasks.isEmpty()) {
                notiWhenEmpty.setVisibility(View.VISIBLE);
                suggestList.setVisibility(View.GONE);
            } else {
                notiWhenEmpty.setVisibility(View.GONE);
                suggestList.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
            dialog.getWindow().setDimAmount(0.5f);
        }
    }

}
