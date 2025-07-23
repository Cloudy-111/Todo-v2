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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskDetailViewModel extends ViewModel {
    private final TaskRepository taskRepository = new TaskRepository();
    private final PriorityRepository priorityRepository = new PriorityRepository();
    private final TagRepository tagRepository = new TagRepository();
    private final CheckListRepository checkListRepository = new CheckListRepository();

    private final MutableLiveData<Task> _task = new MutableLiveData<>();
    public LiveData<Task> taskLiveData = _task;

    private final MutableLiveData<Tag> _tag = new MutableLiveData<>();
    public LiveData<Tag> tagLiveData = _tag;

    private final MutableLiveData<Priority> _priority = new MutableLiveData<>();
    public LiveData<Priority> priorityLiveData = _priority;

    private final MutableLiveData<List<Checklist>> _checklistItems = new MutableLiveData<List<Checklist>>();
    public LiveData<List<Checklist>> checklistItems = _checklistItems;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void loadData(String taskId){
        executor.execute(() -> {
            Task task = taskRepository.getTaskById(taskId);
            _task.postValue(task);
            _checklistItems.postValue(checkListRepository.getAllChecklistByTaskId(taskId));

            if(task != null){
                loadTag(task.getTagId());
                loadPriority(task.getPriorityId());
            }
        });
    }

    public void loadTag(String tagId){
        executor.execute(() -> _tag.postValue(tagRepository.getTagById(tagId)));
    }

    public void loadPriority(String priorityId){
        executor.execute(() -> _priority.postValue(priorityRepository.getPriorityById(priorityId)));
    }

    public void makeTaskCompleted(String taskId, TaskRepository.TaskCallback callback){
        taskRepository.makeTaskCompleted(taskId);
    }

    public void updateChecklistItem(List<String> listItemNeedUpdate, CheckListRepository.CheckListCallback callback){
        checkListRepository.updateChecklistItem(listItemNeedUpdate, callback);
    }

    public void updateProgressTask(String taskId, double successRate){
        taskRepository.updateProgressTask(taskId, successRate);
    }
}
