package sprint4.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private static int id = 0;

    public static int createId() {
        id += 1;
        return id;
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
        tasks.put(task.getId(), task);
    }

    public void createSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtask(subtask.getId());
    }

    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void rmTask(int id) {
        tasks.remove(id);
    }

    public void rmSubtask(int id) {
        epics.get((subtasks.get(id).getEpicId())).removeSubById(id);
        subtasks.remove(id);
    }

    public void rmEpic(int id) {
        ArrayList<Integer> epicValues = epics.get(id).getSubtasks();
        for (Integer value : epicValues) {
            subtasks.remove(value);
        }
        epics.remove(id);
        }


    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        epics.get((subtask.getEpicId())).removeSubById(subtask.getId());
        epics.get(subtask.getEpicId()).getSubtasks().add(subtask.getId());
        epics.put(epics.get(subtask.getEpicId()).getId(), epics.get(subtask.getEpicId()));
        subtasks.put(subtask.getId(), subtask);
        ArrayList<Integer> subtasksValue = epics.get(subtask.getEpicId()).getSubtasks();
        ArrayList<Subtask> subtasksEpics = new ArrayList<>();
        for (Integer subtaskValue : subtasksValue) {
            subtasksEpics.add(subtasks.get(subtaskValue));
        }
        epics.get(subtask.getEpicId()).calcStatus(subtasksEpics);
    }

    public void updateEpic(Epic epic) {
        epic.setSubtasks((epics.get(epic.getId())).getSubtasks());
        epic.calcStatus((ArrayList<Subtask>) subtasks.values());
        epics.put(epic.getId(), epic);
    }

    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        ArrayList<Integer> subtasksId = epics.get(epic.getId()).getSubtasks();
        ArrayList<Subtask> subtasks = new ArrayList<>();
         for (Integer subtask : subtasksId) {
             subtasks.add(this.subtasks.get(subtask));
        }
        return subtasks;
    }

    public Collection<Task> getTasks() {
        return tasks.values();
    }

    public Collection<Subtask> getSubtasks() {
        return subtasks.values();
    }

    public Collection<Epic> getEpics() {
        return epics.values();
    }
}
