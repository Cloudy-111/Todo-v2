package com.example.todo_listv2.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
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

public class DetailNoteFragment extends DialogFragment {
    private FragmentAddNewNoteBinding binding;
    private SharedPreferences preferences;
    private String userId;
    private NotesViewModel notesViewModel;
    private ImageView backButton;
    private View microButton, trashButton;
    private CardView selectColorButton;
    private EditText titleText, contentText;
    private ColorBackgroundAdapter colorBackgroundAdapter;
    private RecyclerView colorRecycler, backgroundRecycler;
    private LinearLayout fragmentRoot;
    private int backgroundId = 0;
    private String colorSelected = "";
    private String noteId;
    private List<Integer> colorList, backgrounds;
    private String[] colorHexList = {"#75f4f4", "#90e0f3", "#b8b3e9", "#d999b9", "#d17b88", "#f49097", "#dfb2f4", "#f5e960", "#f2f5ff", "#55d6c2"};

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
            updateNote();

            notesViewModel.loadNoteList(userId);
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        microButton.setOnClickListener(v -> {

        });

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
                    .setNegativeButton("NO", (((dialog1, which) -> {
                        dialog1.dismiss();
                    })))
                    .setCancelable(false);

            dialog.show();
        });

        selectColorButton.setOnClickListener(v -> {
            showColorBackgroundSelector();
        });
    }

    private void deleteNote(String noteId){
        notesViewModel.deleteNote(noteId);
    }

    private void updateNote(){
        String title = titleText.getText().toString();
        String content = contentText.getText().toString();

        if(!content.isEmpty()){
            Note newNote = new Note(noteId, title, content, colorSelected, backgroundId, userId);
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

            colorSelected = colorHexList[index];
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
}
