package com.example.todo_listv2.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todo_listv2.adapters.ListItemTask;
import com.example.todo_listv2.models.Priority;
import com.example.todo_listv2.models.PriorityHeaderItem;
import com.example.todo_listv2.models.Tag;
import com.example.todo_listv2.models.Task;
import com.example.todo_listv2.models.TaskItemWrapper;
import com.example.todo_listv2.repositories.PriorityRepository;
import com.example.todo_listv2.repositories.TagRepository;
import com.example.todo_listv2.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskDayViewModel extends ViewModel {
    private TaskRepository taskRepository = new TaskRepository();
    private TagRepository tagRepository = new TagRepository();
    private PriorityRepository priorityRepository = new PriorityRepository();

    private final MutableLiveData<List<ListItemTask>> _listItemTasks = new MutableLiveData<>();
    public LiveData<List<ListItemTask>> listItemTasks = _listItemTasks;

    private final MutableLiveData<Map<String, Tag>> _tagMap = new MutableLiveData<>();
    public LiveData<Map<String, Tag>> tagMap = _tagMap;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    public void loadData(String day, String userId){
        executor.execute(() -> {
            List<Task> tasks = taskRepository.getAllTaskByUserIdAndSelectedDay(day, userId);
            List<Tag> tags = tagRepository.getAllTagByUserId(userId);
            List<Priority> priorities = priorityRepository.getAllPriority();

            // Tạo map từ tagId -> Tag để truyền vào adapter
            Map<String, Tag> tagMap = new HashMap<>();
            for (Tag tag : tags) {
                tagMap.put(tag.getId(), tag);
            }
            _tagMap.postValue(tagMap);

            // Tạo danh sách hiển thị cho adapter (listItemTasks)
            List<ListItemTask> result = buildItemList(tasks, priorities);
            _listItemTasks.postValue(result);
        });
    }

    private List<ListItemTask> buildItemList(List<Task> tasks, List<Priority> priorities) {
        List<ListItemTask> result = new ArrayList<>();

        for (Priority priority : priorities) {
            List<Task> tasksForPriority = new ArrayList<>();
            for (Task t : tasks) {
                if (t.getPriorityId().equals(priority.getId())) {
                    tasksForPriority.add(t);
                }
            }
            Log.d("TASK: ", String.valueOf(tasksForPriority.size()));

            if (!tasksForPriority.isEmpty()) {
                result.add(new PriorityHeaderItem(priority)); // Header
                for (Task task : tasksForPriority) {
                    result.add(new TaskItemWrapper(task));
                }

                // Optional: thêm progress bar nếu muốn
//                int completedCount = (int) tasksForPriority.stream().filter(Task::isCompleted).count();
//                result.add(new TaskProgressItem(completedCount, tasksForPriority.size()));
            }
        }

        return result;
    }
}
