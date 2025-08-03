package com.example.todo_listv2.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.adapters.ColorBackgroundAdapter;
import com.example.todo_listv2.databinding.FragmentAddNewNoteBinding;
import com.example.todo_listv2.models.Note;
import com.example.todo_listv2.viewModels.NotesViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DetailNoteFragment extends DialogFragment {
    private FragmentAddNewNoteBinding binding;
    private SharedPreferences preferences;
    private String userId;
    private NotesViewModel notesViewModel;
    private ImageView backButton;
    private View microButton, trashButton;
    private CardView selectColorButton;
    private EditText titleText, contentText;
    private RecyclerView colorRecycler, backgroundRecycler;
    private LinearLayout fragmentRoot;
    private int backgroundId = 0;
    private String colorSelected = "";
    private String noteId;
    private String oldTitleText, oldContentText, oldColorSelected;
    private Integer oldBackgroundId;
    private List<Integer> colorList, backgrounds;
    private final List<String> colorHexList = Arrays.asList("#75f4f4", "#90e0f3", "#b8b3e9", "#d999b9", "#d17b88", "#f49097", "#dfb2f4", "#f5e960", "#f2f5ff", "#55d6c2");

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
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
        noteId = getArguments() != null ? getArguments().getString("noteId") : null;

        colorList = new ArrayList<>();
        for (String hex : colorHexList) {
            colorList.add(Color.parseColor(hex));
        }
        backgrounds = Arrays.asList(
                R.drawable.none_bg,
                R.drawable.bg_1,
                R.drawable.bg_2,
                R.drawable.bg_3,
                R.drawable.bg_4,
                R.drawable.bg_5,
                R.drawable.bg_6,
                R.drawable.bg_7,
                R.drawable.bg_8,
                R.drawable.bg_9,
                R.drawable.bg_10);

        initViews();
        observeData();

        trashButton.setVisibility(View.VISIBLE);

        if(noteId != null){
            notesViewModel.loadNoteData(noteId);
        }

        setOnClickListener();
    }

    private void observeData(){
        notesViewModel.note.observe(getViewLifecycleOwner(), note -> {
            if(note != null){
                titleText.setText(note.getHeader());
                contentText.setText(note.getContent());
                colorSelected = note.getColor();
                backgroundId = note.getBackgroundId();

                oldBackgroundId = backgroundId;
                oldColorSelected = colorSelected;
                oldTitleText = note.getHeader();
                oldContentText = note.getContent();

                if(backgroundId == 0){
                    if(colorSelected != null && !colorSelected.isEmpty()){
                        int indexColor = colorHexList.indexOf(colorSelected);
                        int color = indexColor == -1 ? Color.WHITE : colorList.get(indexColor);
                        fragmentRoot.setBackgroundTintList(ColorStateList.valueOf(color));
                    }
                } else {
                    fragmentRoot.setBackgroundResource(backgrounds.get(backgroundId));
                    if(backgroundId == 4 || backgroundId == 6) {
                        setColorTint(Color.BLACK);
                    } else {
                        setColorTint(Color.WHITE);
                    }
                }

            }
        });
    }

    private void initViews(){
        backButton = binding.backButton;
        microButton = binding.microButton;
        trashButton = binding.trashButton;
        selectColorButton = binding.selectColorButton;
        titleText = binding.titleNoteEdit;
        contentText = binding.contentNoteEdit;

        fragmentRoot = binding.fragmentAddNewNote;
    }

    private void setOnClickListener(){
        backButton.setOnClickListener(v -> {
            String newTitle = titleText.getText().toString();
            String newContent = contentText.getText().toString();

            if(!newContent.equals(oldContentText) || !newTitle.equals(oldTitleText) || oldBackgroundId != backgroundId || oldColorSelected.equals(colorSelected)){
                updateNote(newTitle, newContent);
                notesViewModel.loadNoteList(userId);
            }

            requireActivity().getSupportFragmentManager().popBackStack();
        });

        microButton.setOnClickListener(v -> speechToText());

        trashButton.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure about that?")
                    .setPositiveButton("YES", ((dialog1, which) -> {
                        deleteNote(noteId);
                        dialog1.dismiss();
                        notesViewModel.loadNoteList(userId);
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }))
                    .setNegativeButton("NO", (((dialog1, which) -> dialog1.dismiss())))
                    .setCancelable(false);

            dialog.show();
        });

        selectColorButton.setOnClickListener(v -> showColorBackgroundSelector());
    }

    private void deleteNote(String noteId){
        notesViewModel.deleteNote(noteId);
    }

    private void updateNote(String newTitle, String newContent){
        if(!newContent.isEmpty()){
            Note newNote = new Note(noteId, newTitle, newContent, colorSelected, backgroundId, userId);
            notesViewModel.updateNote(newNote);
        }
    }
    private void showColorBackgroundSelector(){
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bottom_color_background_selector, null);
        dialog.setContentView(view);

        colorRecycler = view.findViewById(R.id.colorRecyclerView);
        backgroundRecycler = view.findViewById(R.id.backgroundRecyclerView);

        colorRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        colorRecycler.setAdapter(new ColorBackgroundAdapter(colorList, true, selectedColor -> {
            fragmentRoot.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
            int index = colorList.indexOf(selectedColor);

            colorSelected = colorHexList.get(index);
        }));

        backgroundRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        backgroundRecycler.setAdapter(new ColorBackgroundAdapter(backgrounds, false, selectedDrawable -> {
            fragmentRoot.setBackgroundTintList(null);
            if(selectedDrawable == R.drawable.none_bg){
                fragmentRoot.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                setColorTint(Color.BLACK);
                backgroundId = 0;
            } else {
                fragmentRoot.setBackgroundResource(selectedDrawable);
                int index = backgrounds.indexOf(selectedDrawable);
                if(index == 4 || index == 6) {
                    setColorTint(Color.BLACK);
                } else {
                    setColorTint(Color.WHITE);
                }
                backgroundId = index;
            }


        }));

        dialog.show();
    }

    private void setColorTint(int color){
        contentText.setTextColor(ColorStateList.valueOf(color));
        titleText.setTextColor(ColorStateList.valueOf(color));
        backButton.setImageTintList(ColorStateList.valueOf(color));
    }

    private void speechToText(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // Create Intent to start S2T

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to Text");

        try {
            startActivityForResult(
                    intent, REQUEST_CODE_SPEECH_INPUT
            );
        } catch (Exception e){
            Toast.makeText(getContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    @Deprecated
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result
                        = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);

                if (result != null && !result.isEmpty()) {
                    contentText.setText(Objects.requireNonNull(
                            result.get(0)));
                }
            }
        }
    }
}
