package com.example.todo_listv2.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todo_listv2.models.Note;
import com.example.todo_listv2.repositories.NoteRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotesViewModel extends ViewModel {
    private NoteRepository noteRepository = new NoteRepository();

    private MutableLiveData<List<Note>> _listNote = new MutableLiveData<>();
    public LiveData<List<Note>> listNote = _listNote;

    private MutableLiveData<Note> _note = new MutableLiveData<>();
    private LiveData<Note> note = _note;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void loadNoteList(String userid){
        executor.execute(() -> {
            _listNote.postValue(noteRepository.loadAllNote(userid));
        });
    }

    public void saveNewNote(Note note){
        noteRepository.createNewNote(note);
    }
}
