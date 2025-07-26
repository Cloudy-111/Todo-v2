package com.example.todo_listv2.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todo_listv2.adapters.ListItemTask;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.models.TaskItemWrapper;
import com.example.todo_listv2.repositories.TagRepository;
import com.example.todo_listv2.repositories.TaskRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TagDetailViewModel extends ViewModel {
    public enum FilterType {
        ALL,
        OVERDUE,
        COMPLETED,
        WILL_DO
    }
    private final TaskRepository taskRepository = new TaskRepository();
    private final TagRepository tagRepository = new TagRepository();

    private MutableLiveData<List<ListItemTask>> _listItemTasks = new MutableLiveData<>();
    public LiveData<List<ListItemTask>> listItemTask = _listItemTasks;

    private MutableLiveData<Tag> _tag = new MutableLiveData<>();
    public LiveData<Tag> tag = _tag;

    private final MutableLiveData<Map<String, Tag>> _tagMap = new MutableLiveData<>();
    public LiveData<Map<String, Tag>> tagMap = _tagMap;

    private final MutableLiveData<FilterType> _filter = new MutableLiveData<>(FilterType.ALL);
    public LiveData<FilterType> filter = _filter;

    private final MediatorLiveData<List<ListItemTask>> _visibleItems = new MediatorLiveData<>();
    public LiveData<List<ListItemTask>> visibleItems = _visibleItems;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public TagDetailViewModel(){
        _visibleItems.addSource(_listItemTasks, tasks -> reloadVisible(tasks, _filter.getValue()));
        _visibleItems.addSource(_filter, f -> reloadVisible(_listItemTasks.getValue(), f));
    }

    public void setFilter(FilterType filter){
        _filter.setValue(filter);
    }

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

    public void saveEditTag(Tag tag){
        tagRepository.saveEditTag(tag, new TagRepository.TagCallback() {
            @Override
            public void onSuccess(String message, Tag updatedTag) {
                _tag.postValue(updatedTag);
                Map<String, Tag> map = _tagMap.getValue();
                if (map == null) map = new HashMap<>();
                map.put(updatedTag.getId(), updatedTag);
                _tagMap.postValue(map);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    public void reloadVisible(List<ListItemTask> listItemTasks, FilterType filterType){
        if(listItemTasks == null || listItemTasks.isEmpty()){
            _visibleItems.setValue(Collections.emptyList());
            return;
        }
        if(filterType == null || filterType == FilterType.ALL){
            _visibleItems.setValue(listItemTasks);
            return;
        }

        List<ListItemTask> filtered = new ArrayList<>();
        long timeNow = System.currentTimeMillis();
        for(ListItemTask item : listItemTasks){
            if(item instanceof TaskItemWrapper){
                Task task = ((TaskItemWrapper) item).getTask();
                if(filterType == FilterType.OVERDUE){
                    if(!task.isCompleted() && task.getEndTime() < timeNow) filtered.add(item);
                } else if(filterType == FilterType.COMPLETED){
                    if(task.isCompleted()) filtered.add(item);
                } else if(filterType == FilterType.WILL_DO){
                    if(!task.isCompleted() && task.getStartTime() > timeNow) filtered.add(item);
                }
            }
        }
        _visibleItems.setValue(filtered);

    }
}
