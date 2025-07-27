package com.example.todo_listv2.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todo_listv2.adapters.ListItemTask;
import com.example.todo_listv2.models.Priority;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.models.TaskItemWrapper;
import com.example.todo_listv2.repositories.PriorityRepository;
import com.example.todo_listv2.repositories.TagRepository;
import com.example.todo_listv2.repositories.TaskRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public enum SortType {
        PRIORITY,
        IS_COMPLETED,
        CLOSEST,
        START_DATE
    }
    private final TaskRepository taskRepository = new TaskRepository();
    private final TagRepository tagRepository = new TagRepository();
    private final PriorityRepository priorityRepository = new PriorityRepository();

    private MutableLiveData<List<ListItemTask>> _listItemTasks = new MutableLiveData<>();
    public LiveData<List<ListItemTask>> listItemTask = _listItemTasks;

    private MutableLiveData<Tag> _tag = new MutableLiveData<>();
    public LiveData<Tag> tag = _tag;

    private final MutableLiveData<Map<String, Tag>> _tagMap = new MutableLiveData<>();
    public LiveData<Map<String, Tag>> tagMap = _tagMap;

    private final MutableLiveData<FilterType> _filter = new MutableLiveData<>(FilterType.ALL);
    public LiveData<FilterType> filter = _filter;

    private final MutableLiveData<SortType> _sortType = new MutableLiveData<>(SortType.CLOSEST);
    public LiveData<SortType> sortTypeLiveData = _sortType;

    private final MediatorLiveData<List<ListItemTask>> _visibleItems = new MediatorLiveData<>();
    public LiveData<List<ListItemTask>> visibleItems = _visibleItems;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private Map<String, Priority> priorityMap = new HashMap<>();

    public TagDetailViewModel(){
        _visibleItems.addSource(_listItemTasks, tasks -> reloadVisible(tasks, _filter.getValue(), _sortType.getValue()));
        _visibleItems.addSource(_filter, f -> reloadVisible(_listItemTasks.getValue(), f, _sortType.getValue()));
        _visibleItems.addSource(_sortType, s -> reloadVisible(_listItemTasks.getValue(), _filter.getValue(), s));
    }

    public void setFilter(FilterType filter){ _filter.setValue(filter); }
    public void setSortType(SortType sortType){ _sortType.setValue(sortType); }

    public void loadData(String tagId, String userId){
        executor.execute(() -> {
            Tag tag = tagRepository.getTagById(tagId);
            _tag.postValue(tag);
            List<Task> taskList = taskRepository.getAllTaskByTagId(tagId, userId);
            List<Priority> priorities = priorityRepository.getAllPriority();

            Map<String, Priority> tempPriorityMap = new HashMap<>();
            for (Priority p : priorities) {
                tempPriorityMap.put(p.getId(), p);
            }
            priorityMap = tempPriorityMap;

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

    private int priorityLevelOf(Task t) {
        Priority p = priorityMap != null ? priorityMap.get(t.getPriorityId()) : null;
        return p != null ? p.getLevel() : Integer.MIN_VALUE;
    }

    public void reloadVisible(List<ListItemTask> listItemTasks, FilterType filterType, SortType sortType){
        if(listItemTasks == null || listItemTasks.isEmpty()){
            _visibleItems.setValue(Collections.emptyList());
            return;
        }

        List<ListItemTask> filtered = applyFilter(listItemTasks, filterType);

        List<ListItemTask> sorted = sortItems(filtered, sortType);

        _visibleItems.setValue(sorted);

    }

    private List<ListItemTask> applyFilter(List<ListItemTask> listItemTasks, FilterType filterType){
        if (filterType == null || filterType == FilterType.ALL) return listItemTasks;

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
        return filtered;
    }

    private List<ListItemTask> sortItems(List<ListItemTask> listItemTasks, SortType sortType){
        if (sortType == null) sortType = SortType.CLOSEST;

        List<ListItemTask> result = new ArrayList<>(listItemTasks);

        Comparator<Task> byPriorityDesc =
                (t1, t2) -> Integer.compare(priorityLevelOf(t2), priorityLevelOf(t1));

        Comparator<Task> byClosestEnd =
                Comparator.comparingLong(Task::getEndTime);

        Comparator<Task> byStartDate =
                Comparator.comparingLong(Task::getStartTime);

        Comparator<Task> byCompleted =
                (t1, t2) -> Boolean.compare(t1.isCompleted(), t2.isCompleted());

        SortType finalSortType = sortType;
        result.sort((i1, i2) -> {
            if (!(i1 instanceof TaskItemWrapper) || !(i2 instanceof TaskItemWrapper)) return 0;

            Task t1 = ((TaskItemWrapper) i1).getTask();
            Task t2 = ((TaskItemWrapper) i2).getTask();

            switch (finalSortType) {
                case PRIORITY:
                    return byPriorityDesc.compare(t1, t2);
                case IS_COMPLETED:
                    return byCompleted.compare(t1, t2);
                case START_DATE:
                    return byStartDate.compare(t1, t2);
                case CLOSEST:
                default:
                    return byClosestEnd.compare(t1, t2);
            }
        });

        return result;
    }
}
