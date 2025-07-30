package com.example.todo_listv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_listv2.R;
import com.example.todo_listv2.models.Note;
import com.example.todo_listv2.viewHolders.NoteItemViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteItemViewHolder> {
    private List<Note> noteList;
    private OnNoteItemClickListener listener;

    public interface OnNoteItemClickListener{
        void onNoteItemClick(Note note);
    }

    public NotesAdapter(List<Note> noteList, OnNoteItemClickListener listener){
        this.noteList = noteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quick_note, parent, false);
        return new NoteItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteItemViewHolder viewHolder, int position){
        Note note = noteList.get(position);

        viewHolder.bind(note);
    }

    @Override
    public int getItemCount(){return noteList.size();}

    public void setNotes(List<Note> list){
        List<Note> old = this.noteList != null ? this.noteList : Collections.emptyList();
        List<Note> newList = list != null ? list : new ArrayList<>();

        DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new NoteDiffCallback(old, newList));
        this.noteList = newList;
        diff.dispatchUpdatesTo(this);
    }
}
