import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    public Collection<Task> getTasks() {
        return tasks.values();
    }

    public Collection<Subtask> getSubtasks() {
        return subtasks.values();
    }

    public Collection<Epic> getEpics() {
        return epics.values();
    }
    public void rmTasks() {
        tasks.clear();
    }

    public void rmSubtasks() {
        subtasks.clear();
    }

    public void rmEpics() {
        subtasks.clear();
        epics.clear();
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void createTask(Task task) {
        tasks.put(task.id, task);
    }

    public void createSubtask(Subtask subtask) {
        subtasks.put(subtask.id, subtask);
        (subtask.epic).addSubtask(subtask);
    }

    public void createEpic(Epic epic) {
        epics.put(epic.id, epic);
    }

    public void rmTask(int id) {
        tasks.remove(id);
    }

    public void rmSubtask(int id) {
        subtasks.get(id).epic.removeSubById(id);
        subtasks.remove(id);
    }

    public void rmEpic(int id) {
        for (Subtask subtask : subtasks.values()) {
            if (subtasks.containsValue(subtask)) {
                subtasks.remove(subtask.getId());
            }
        }
        epics.remove(id);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtask.epic.removeSubById(subtask.id);
        subtask.epic.subtasks.add(subtask);
        subtask.epic.status = subtask.epic.getStatus();
        epics.put(subtask.epic.getId(), subtask.epic);
        subtasks.put(subtask.getId(), subtask);

    }

    public void updateEpic(Epic epic) {
        epic.subtasks = (epics.get(epic.getId())).subtasks;
        epic.status = epic.getStatus();
        epics.put(epic.getId(), epic);
    }

    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        return epics.get(epic.id).getSubtasks();
    }
}
