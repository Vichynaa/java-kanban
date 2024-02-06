package sprint4.managers;

import sprint4.models.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static sprint4.models.Status.*;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private static int id = 0;

    public static int createId() {
        id += 1;
        return id;
    }

    public void removeTasks() {
        tasks.clear();
    }

    public void removeSubtasks() {
        subtasks.clear();
    }

    public void removeEpics() {
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
        epics.get(subtask.getEpicId()).getSubtasks().add(subtask.getId());
    }

    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeSubtask(int id) {
        Epic epic = epics.get((subtasks.get(id).getEpicId()));
        epic.getSubtasks().remove(id);
        subtasks.remove(id);
        epic.setStatus(calcStatus(epic));
    }

    public void removeEpic(int id) {
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
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasks().remove(subtask.getId());
        epic.getSubtasks().add(subtask.getId());
        epics.put(epic.getId(), epic);
        subtasks.put(subtask.getId(), subtask);
        epic.setStatus(calcStatus(epic));
    }

    public void updateEpic(Epic epic) {
        epic.setSubtasks((epics.get(epic.getId())).getSubtasks());
        epic.setStatus(calcStatus(epic));
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

    private Status calcStatus(Epic epic) {
        boolean statusNew = false;
        boolean statusDone = false;

        for (Integer idTask : epic.getSubtasks()) {
            Subtask subtask = this.subtasks.get(idTask);
            Status curStatus = subtask.getStatus();
            if (curStatus == IN_PROGRESS) {
                return IN_PROGRESS;
            } else if (curStatus == NEW) {
                statusNew = true;
            } else if (curStatus == DONE) {
                statusDone = true;
            }
            if (statusNew && statusDone) {
                return IN_PROGRESS;
            }
        }
        if (statusDone) {
            return DONE;
        } else {
            return NEW;
        }
    }
}
