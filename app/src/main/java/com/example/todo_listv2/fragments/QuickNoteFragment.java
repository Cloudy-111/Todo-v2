package com.example.todo_listv2.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.todo_listv2.R;
import com.example.todo_listv2.adapters.NotesAdapter;
import com.example.todo_listv2.databinding.FragmentQuickNoteBinding;
import com.example.todo_listv2.viewModels.NotesViewModel;

import java.util.ArrayList;

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

        initViews();
        observeData();
        setupRecycler();
        btnSwitchLayout.setOnClickListener(v -> {
            isGridMode = !isGridMode;
            switchLayout();
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
        notesAdapter = new NotesAdapter(new ArrayList<>(), note -> {

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
