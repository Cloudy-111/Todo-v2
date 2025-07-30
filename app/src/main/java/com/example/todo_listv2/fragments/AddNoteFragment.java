package com.example.todo_listv2.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.todo_listv2.databinding.FragmentAddNewNoteBinding;
import com.example.todo_listv2.models.Note;
import com.example.todo_listv2.viewModels.NotesViewModel;

public class AddNoteFragment extends DialogFragment {
    private FragmentAddNewNoteBinding binding;
    private SharedPreferences preferences;
    private String userId;
    private NotesViewModel notesViewModel;
    private ImageView backButton;
    private View microButton;
    private CardView selectColorButton;
    private EditText titleText, contentText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState){
        binding = FragmentAddNewNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = preferences.getString("user_id", "1");
        notesViewModel = new ViewModelProvider(getActivity()).get(NotesViewModel.class);

        initViews();
        setOnClickListener();
    }

    private void initViews(){
        backButton = binding.backButton;
        microButton = binding.microButton;
        selectColorButton = binding.selectColorButton;
        titleText = binding.titleNoteEdit;
        contentText = binding.contentNoteEdit;
    }

    private void setOnClickListener(){
        backButton.setOnClickListener(v -> {
            saveNewNote();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        microButton.setOnClickListener(v -> {

        });

        selectColorButton.setOnClickListener(v -> {

        });
    }

    private void saveNewNote(){
        String title = titleText.getText().toString();
        String content = contentText.getText().toString();

        if(!content.isEmpty()){
            Note newNote = new Note(title, content, "", 0, userId);
            Log.d("NOTE", newNote.toString());
            notesViewModel.saveNewNote(newNote);
        }
    }
}
