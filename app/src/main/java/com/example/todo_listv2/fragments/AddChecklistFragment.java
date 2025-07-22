package com.example.todo_listv2.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.adapters.CheckListItemAdapter;
import com.example.todo_listv2.databinding.FragmentAddChecklistBinding;
import com.example.todo_listv2.models.Checklist;
import com.example.todo_listv2.viewModels.AddTaskViewModel;

public class AddChecklistFragment extends Fragment {
    private FragmentAddChecklistBinding binding;
    private SharedPreferences preferences;
    private RecyclerView checkListRecycler;
    private TextView addCheckListItem;
    private Button doneButton, confirmButton;
    private ImageView backButton;
    private EditText checklistContent;
    private AddTaskViewModel addTaskViewModel;
    private CheckListItemAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentAddChecklistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "1");

        checkListRecycler = binding.listCheckItem;
        addCheckListItem = binding.addNewCheckListButton;
        backButton = binding.backButton;
        doneButton = binding.doneButton;

        adapter = new CheckListItemAdapter();
        checkListRecycler.setAdapter(adapter);
        checkListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        addTaskViewModel = new ViewModelProvider(requireActivity()).get(AddTaskViewModel.class);
        observeData();

        addCheckListItem.setOnClickListener(v -> showChecklistInputDialog());

        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
        });

        doneButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
        });
    }

    private void observeData(){
        addTaskViewModel.checkListItems.observe(getViewLifecycleOwner(), items -> {
            adapter.updateData(items);
        });
    }

    private void showChecklistInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView= LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_checklist_item, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        confirmButton = dialogView.findViewById(R.id.btn_confirm);
        checklistContent = dialogView.findViewById(R.id.enter_checklist_content);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = checklistContent.getText().toString();
                if(!content.isEmpty()){
                    addTaskViewModel.addCheckList(new Checklist(content));
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
