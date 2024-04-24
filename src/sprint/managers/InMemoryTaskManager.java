package sprint.managers;

import sprint.exceptions.ValidateException;
import sprint.models.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static sprint.models.Status.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final HistoryManager history = (new Managers()).getDefaultHistory();
    protected static int id = 0;

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
        boolean taskValidator = getPrioritizedTasks().stream()
                        .anyMatch(taskObject -> validate(taskObject, task));
        if (!taskValidator) {
            tasks.put(task.getId(), task);
        } else {
            throw new ValidateException("Выполняется другая задача в это время");
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        boolean subtaskValidator = getPrioritizedTasks().stream()
                .anyMatch(taskObject -> validate(taskObject, subtask));
        if (!subtaskValidator) {
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).setSubtask(subtask);
            calcEpicTime(epics.get(subtask.getEpicId()));
            epics.get(subtask.getEpicId()).setStatus(calcStatus(epics.get(subtask.getEpicId())));
        } else {
            throw new ValidateException("Выполняется другая задача в это время");
        }
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
        calcEpicTime(epic);
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
        LocalDateTime taskTime = tasks.get(task.getId()).getStartTime();
        tasks.get(task.getId()).setStartTime(null);
        boolean taskUpdateValidator = getPrioritizedTasks().stream()
                .anyMatch(taskObject -> validate(taskObject, task));
        if (!taskUpdateValidator) {
            tasks.put(task.getId(), task);
        } else {
            tasks.get(task.getId()).setStartTime(taskTime);
            throw new ValidateException("Выполняется другая задача в это время - обновление невозможно");
        }

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        LocalDateTime subtaskTime = subtasks.get(subtask.getId()).getStartTime();
        subtasks.get(subtask.getId()).setStartTime(null);
        boolean subtaskUpdateValidator = getPrioritizedTasks().stream()
                .anyMatch(taskObject -> validate(taskObject, subtask));
        if (!subtaskUpdateValidator) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasks().remove(subtask.getId());
            epic.getSubtasks().add(subtask.getId());
            epics.put(epic.getId(), epic);
            subtasks.put(subtask.getId(), subtask);
            epic.setStatus(calcStatus(epic));
            calcEpicTime(epic);
        } else {
            subtasks.get(subtask.getId()).setStartTime(subtaskTime);
            throw new ValidateException("Выполняется другая задача в это время - обновление невозможно");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epic.setSubtasks((epics.get(epic.getId())).getSubtasks());
        epic.setStatus(calcStatus(epic));
        calcEpicTime(epic);
        epics.put(epic.getId(), epic);
    }

    @Override
    public List<Subtask> getEpicSubtasks(Epic epic) {
        List<Integer> subtasksId = epics.get(epic.getId()).getSubtasks();
        return subtasksId.stream()
                .map(subtasks::get)
                .toList();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }


    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Task> getHistory() {
        return history.getHistory();
    }

    public static void setId(int id) {
        InMemoryTaskManager.id = id;
    }

    public Set<Integer> getKeysTasks() {
        return tasks.keySet();
    }

    public boolean validate(Task task1, Task task2) {
        LocalDateTime startTime1 = task1.getStartTime();
        LocalDateTime endTime1 = task1.getEndTime();
        LocalDateTime startTime2 = task2.getStartTime();
        LocalDateTime endTime2 = task2.getEndTime();

        if (startTime1 == null || endTime1 == null || startTime2 == null || endTime2 == null) {
            return false;
        }
        return !(endTime1.isBefore(startTime2) || startTime1.isAfter(endTime2));
    }

    public TreeSet<Task> getPrioritizedTasks() {
        Comparator<Task> comparator = Comparator.comparing(Task::getStartTime);
        TreeSet<Task> priority = new TreeSet<>(comparator);
        List<Task> tasks = getTasks().stream()
                .filter(task -> task.getStartTime() != null)
                .toList();
        priority.addAll(tasks);
        List<Subtask> subtasks = getSubtasks().stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .toList();
        priority.addAll(subtasks);
        List<Epic> epics = getEpics().stream()
                .filter(epic -> epic.getStartTime() != null)
                .toList();
        priority.addAll(epics);
        return priority;
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

    public void calcEpicTime(Epic epic) {
        LocalDateTime startTime = epic.getSubtasks().stream()
                .map(subtasks::get)
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime endTime = epic.getSubtasks().stream()
                .map(subtasks::get)
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        if (startTime != null && endTime != null) {
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            epic.setDuration(Duration.between(epic.getStartTime(), epic.getEndTime()));
        }
    }
}
