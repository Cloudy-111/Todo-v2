package com.example.todo_listv2.fragments;

import static android.content.Context.MODE_PRIVATE;

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
import androidx.fragment.app.Fragment;
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

public class AddNoteFragment extends DialogFragment {
    private FragmentAddNewNoteBinding binding;
    private SharedPreferences preferences;
    private String userId;
    private NotesViewModel notesViewModel;
    private ImageView backButton;
    private View microButton;
    private CardView selectColorButton;
    private EditText titleText, contentText;
    private ColorBackgroundAdapter colorBackgroundAdapter;
    private RecyclerView colorRecycler, backgroundRecycler;
    private LinearLayout fragmentRoot;
    private int backgroundId = 0;
    private String colorSelected = "";

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

        fragmentRoot = binding.fragmentAddNewNote;
    }

    private void setOnClickListener(){
        backButton.setOnClickListener(v -> {
            saveNewNote();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        microButton.setOnClickListener(v -> {

        });

        selectColorButton.setOnClickListener(v -> {
            showColorBackgroundSelector();
        });
    }

    private void saveNewNote(){
        String title = titleText.getText().toString();
        String content = contentText.getText().toString();

        if(!content.isEmpty()){
            Note newNote = new Note(title, content, colorSelected, backgroundId, userId);
            Log.d("NOTE", newNote.toString());
            notesViewModel.saveNewNote(newNote);
        }
    }
    private void showColorBackgroundSelector(){
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bottom_color_background_selector, null);
        dialog.setContentView(view);

        colorRecycler = view.findViewById(R.id.colorRecyclerView);
        backgroundRecycler = view.findViewById(R.id.backgroundRecyclerView);

        String[] colorHexList = {"#75f4f4", "#90e0f3", "#b8b3e9", "#d999b9", "#d17b88", "#f49097", "#dfb2f4", "#f5e960", "#f2f5ff", "#55d6c2"};
        List<Integer> colorList = new ArrayList<>();
        for (String hex : colorHexList) {
            colorList.add(Color.parseColor(hex));
        }
        List<Integer> backgrounds = Arrays.asList(
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
