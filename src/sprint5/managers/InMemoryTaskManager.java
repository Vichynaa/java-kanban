package sprint5.managers;

import sprint5.models.*;

import java.util.ArrayList;
import java.util.HashMap;

import static sprint5.models.Status.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager history = Managers.getDefaultHistory();
    private static int id = 0;

    public static int createId() {
        id += 1;
        return id;
    }

    @Override
    public void removeTasks() {
        tasks.clear();
    }

    @Override
    public void removeSubtasks() {
        subtasks.clear();
    }

    @Override
    public void removeEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        history.add(task);
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        history.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        history.add(epic);
        return epic;
    }

    @Override
    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).setSubtask(subtask);
    }

    @Override
    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeSubtask(int id) {
        Epic epic = epics.get((subtasks.get(id).getEpicId()));
        epic.getSubtasks().remove(id);
        subtasks.remove(id);
        epic.setStatus(calcStatus(epic));
    }

    @Override
    public void removeEpic(int id) {
        ArrayList<Integer> epicValues = epics.get(id).getSubtasks();
        for (Integer value : epicValues) {
            subtasks.remove(value);
        }
        epics.remove(id);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasks().remove(subtask.getId());
        epic.getSubtasks().add(subtask.getId());
        epics.put(epic.getId(), epic);
        subtasks.put(subtask.getId(), subtask);
        epic.setStatus(calcStatus(epic));
    }

    @Override
    public void updateEpic(Epic epic) {
        epic.setSubtasks((epics.get(epic.getId())).getSubtasks());
        epic.setStatus(calcStatus(epic));
        epics.put(epic.getId(), epic);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        ArrayList<Integer> subtasksId = epics.get(epic.getId()).getSubtasks();
        ArrayList<Subtask> subtasks = new ArrayList<>();
        for (Integer subtask : subtasksId) {
            subtasks.add(this.subtasks.get(subtask));
        }
        return subtasks;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }


    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history.getHistory());
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