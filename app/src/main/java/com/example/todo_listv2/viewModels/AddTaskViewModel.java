package com.example.todo_listv2.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todo_listv2.models.Priority;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.repositories.PriorityRepository;
import com.example.todo_listv2.repositories.TagRepository;
import com.example.todo_listv2.repositories.TaskRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddTaskViewModel extends ViewModel {
    private final TaskRepository taskRepository = new TaskRepository();
    private final TagRepository tagRepository = new TagRepository();
    private final PriorityRepository priorityRepository = new PriorityRepository();

    private MutableLiveData<List<Tag>> _tagList = new MutableLiveData<>();
    public LiveData<List<Tag>> tagList = _tagList;
    private MutableLiveData<List<Priority>> _priorityList = new MutableLiveData<>();
    public LiveData<List<Priority>> priorityList = _priorityList;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

//    public void saveNewTask(Task task, TaskRepository.TaskCallback callback){
//        taskRepository.saveNewTask(task, callback);
//    }
//
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
}
