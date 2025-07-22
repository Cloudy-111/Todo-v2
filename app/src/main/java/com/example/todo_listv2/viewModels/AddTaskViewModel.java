package com.example.todo_listv2.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todo_listv2.models.Checklist;
import com.example.todo_listv2.models.Priority;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.repositories.CheckListRepository;
import com.example.todo_listv2.repositories.PriorityRepository;
import com.example.todo_listv2.repositories.TagRepository;
import com.example.todo_listv2.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTaskViewModel extends ViewModel {
    private final TaskRepository taskRepository = new TaskRepository();
    private final TagRepository tagRepository = new TagRepository();
    private final PriorityRepository priorityRepository = new PriorityRepository();
    private final CheckListRepository checkListRepository = new CheckListRepository();

    private MutableLiveData<List<Tag>> _tagList = new MutableLiveData<>();
    public LiveData<List<Tag>> tagList = _tagList;
    private MutableLiveData<List<Priority>> _priorityList = new MutableLiveData<>();
    public LiveData<List<Priority>> priorityList = _priorityList;
    private MutableLiveData<List<Checklist>> _checkListItems = new MutableLiveData<>();
    public LiveData<List<Checklist>> checkListItems = _checkListItems;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void saveNewTask(Task task, TaskRepository.TaskCallback callback){
        taskRepository.saveNewTask(task, callback);
    }

    public void saveNewTag(Tag tag, TagRepository.TagCallback callback){
        tagRepository.saveNewTag(tag, callback);
    }
    public void loadAllTags(String userId){
        executor.execute(() -> {
            _tagList.postValue(tagRepository.getAllTagByUserId(userId));
        });
    }

    public void loadAllPriorities(){
        executor.execute(() -> {
            _priorityList.postValue(priorityRepository.getAllPriority());
        });
    }

    public void addCheckList(Checklist newItem){
        List<Checklist> currentList = _checkListItems.getValue();
        if (currentList == null) currentList = new ArrayList<>();
        currentList.add(newItem);
        _checkListItems.setValue(currentList);
    }

    public void insertChecklistInDB(Checklist item){
        checkListRepository.insertCheckList(item, new CheckListRepository.CheckListCallback() {
            @Override
            public void onSuccess(String message) {
                Log.d("Checklist", message);
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("Checklist Error", errorMessage);
            }
        });
    }

    public void removeCheckList(int position){
        List<Checklist> currentList = _checkListItems.getValue();
        if (currentList != null && position >= 0 && position < currentList.size()) {
            currentList.remove(position);
            _checkListItems.setValue(currentList);
        }
    }

    public List<Checklist> getCurrentChecklist(){
        List<Checklist> currentList = _checkListItems.getValue();
        return currentList != null ? currentList : new ArrayList<>();
    }

    public void clearChecklist() {
        _checkListItems.setValue(new ArrayList<>());
    }
}
