package sprint.managers;

import sprint.exceptions.ManagerSaveException;
import sprint.models.Epic;
import sprint.models.Subtask;
import sprint.models.Task;

import java.util.Collection;
import java.util.List;

public interface TaskManager {
    void removeTasks();

    void removeSubtasks();

    void removeEpics();

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    void createTask(Task task);

    void createSubtask(Subtask subtask);

    void createEpic(Epic epic);

    void removeTask(int id);

    void removeSubtask(int id);

    void removeEpic(int id);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    List<Subtask> getEpicSubtasks(Epic epic);

    Collection<Task> getTasks();

    Collection<Subtask> getSubtasks();

    Collection<Epic> getEpics();
}
