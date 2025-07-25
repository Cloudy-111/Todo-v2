package com.example.todo_listv2.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todo_listv2.adapters.ListItemTask;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.models.TaskItemWrapper;
import com.example.todo_listv2.repositories.TagRepository;
import com.example.todo_listv2.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TagDetailViewModel extends ViewModel {
    private final TaskRepository taskRepository = new TaskRepository();
    private final TagRepository tagRepository = new TagRepository();

    private MutableLiveData<List<ListItemTask>> _listItemTasks = new MutableLiveData<>();
    public LiveData<List<ListItemTask>> listItemTask = _listItemTasks;

    private MutableLiveData<Tag> _tag = new MutableLiveData<>();
    public LiveData<Tag> tag = _tag;

    private final MutableLiveData<Map<String, Tag>> _tagMap = new MutableLiveData<>();
    public LiveData<Map<String, Tag>> tagMap = _tagMap;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void loadData(String tagId, String userId){
        executor.execute(() -> {
            Tag tag = tagRepository.getTagById(tagId);
            _tag.postValue(tag);
            List<Task> taskList = taskRepository.getAllTaskByTagId(tagId, userId);

            Map<String, Tag> tagMap = new HashMap<>();
            tagMap.put(tag.getId(), tag);

            List<ListItemTask> result = buildItemList(taskList);
            _listItemTasks.postValue(result);
        });
    }

    private List<ListItemTask> buildItemList(List<Task> taskList){
        List<ListItemTask> result = new ArrayList<>();
        for(Task task : taskList){
            result.add(new TaskItemWrapper(task));
        }
        return result;
    }
}
