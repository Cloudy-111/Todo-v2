package com.example.todo_listv2.repositories;

import com.example.todo_listv2.fake.fakeDB;
import com.example.todo_listv2.models.Note;

import java.util.List;

import okhttp3.OkHttpClient;

public class NoteRepository {
    private static OkHttpClient client = new OkHttpClient();
    private static String baseURL = "http://192.168.10.104:5000";

    public List<Note> loadAllNote(String userId){
        return fakeDB.getAllNote(userId);
    }

    public void createNewNote(Note note){

    }

    public void deleteNote(String noteId){

    }

    public void updateNote(Note note){

    }

//    public Note loadNote(String noteId){
//
//    }
}
