package com.example.todo_listv2.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.todo_listv2.R;
import com.example.todo_listv2.adapters.NotesAdapter;
import com.example.todo_listv2.databinding.FragmentQuickNoteBinding;
import com.example.todo_listv2.viewModels.NotesViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuickNoteFragment extends Fragment {
    private FragmentQuickNoteBinding binding;
    private SharedPreferences preferences;
    private String userId;
    private RecyclerView noteList;
    private CardView addNoteButton;
    private NotesViewModel notesViewModel;
    private NotesAdapter notesAdapter;
    private boolean isGridMode = true;
    private ImageButton btnSwitchLayout;
    private List<Integer> colorList, backgrounds;
    String[] colorHexList = {"#75f4f4", "#90e0f3", "#b8b3e9", "#d999b9", "#d17b88", "#f49097", "#dfb2f4", "#f5e960", "#f2f5ff", "#55d6c2"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState){
        binding = FragmentQuickNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = preferences.getString("user_id", "1");
        notesViewModel = new ViewModelProvider(getActivity()).get(NotesViewModel.class);

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
        setupRecycler();

        btnSwitchLayout.setOnClickListener(v -> {
            isGridMode = !isGridMode;
            switchLayout();
        });

        addNoteButton.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();

            transaction.replace(R.id.frame_replacement, new AddNoteFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        notesViewModel.loadNoteList(userId);
    }

    private void initViews(){
        noteList = binding.noteRecycle;
        addNoteButton = binding.addQuickNoteButton;
        btnSwitchLayout = binding.btnSwitchLayout;
    }

    private void observeData(){
        notesViewModel.listNote.observe(getViewLifecycleOwner(), notes -> {
            notesAdapter.setNotes(notes);
        });
    }

    private void setupRecycler(){
        notesAdapter = new NotesAdapter(new ArrayList<>(), colorList, backgrounds, noteId -> {
            DetailNoteFragment fragment = new DetailNoteFragment();

            Bundle args = new Bundle();
            args.putString("noteId", noteId);
            fragment.setArguments(args);

            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();

            transaction.replace(R.id.frame_replacement, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        int spanCount = 2;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        noteList.setLayoutManager(layoutManager);
        noteList.setAdapter(notesAdapter);
    }

    private void switchLayout(){
        if (isGridMode) {
            StaggeredGridLayoutManager gridLayout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            noteList.setLayoutManager(gridLayout);
            btnSwitchLayout.setImageResource(R.drawable.ic_list);
        } else {
            LinearLayoutManager listLayout = new LinearLayoutManager(requireContext());
            noteList.setLayoutManager(listLayout);
            btnSwitchLayout.setImageResource(R.drawable.ic_grid);
        }

        noteList.setAdapter(notesAdapter);
    }
}
