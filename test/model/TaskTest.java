package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint.exceptions.ValidateException;
import sprint.managers.HistoryManager;
import sprint.managers.InMemoryTaskManager;
import sprint.managers.Managers;
import sprint.managers.TaskManager;
import sprint.models.Status;
import sprint.models.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskTest {

    private TaskManager taskManager;
    private HistoryManager historyManager;
    private Task task;

    @BeforeEach
    void beforeEach() {
        InMemoryTaskManager.setId(0);
        taskManager = new InMemoryTaskManager();
        historyManager = (new Managers()).getDefaultHistory();
        task = new Task("Test addNewTask", "Test addNewTask description");
    }

    @Test
    void tasksShouldBeEqualsIfSameId() {
        Task task2 = new Task(1, "testTask2", "testTask2", Status.IN_PROGRESS);
        assertEquals(task, task2);
    }

    @Test
    void createNewTask() {
        taskManager.createTask(task);

        final Task savedTask = taskManager.getTask(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final ArrayList<Task> tasks = (ArrayList<Task>)taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addTaskInHistory() {
        historyManager.add(task);
        final ArrayList<Task> history = (ArrayList<Task>) historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void checkTimeValidateFunction() {
        Task task1 = new Task(1, "testTask1", "testTask1", Status.IN_PROGRESS, 60, LocalDateTime.of(2024, 4, 7, 12, 30));
        taskManager.createTask(task1);
        Assertions.assertThrows(ValidateException.class, () -> {
            Task task2 = new Task(2, "testTask2", "testTask2", Status.IN_PROGRESS, 60, LocalDateTime.of(2024, 4, 7, 12, 30));
            taskManager.createTask(task2);
        }, "Две задачи в одно время должны приводить к исключению");
    }

    @Test
    void checkTimeFunction() {
        Task task1 = new Task(1, "testTask1", "testTask1", Status.IN_PROGRESS, 60, LocalDateTime.of(2024, 4, 7, 12, 30));
        taskManager.createTask(task1);
        assertEquals(task1.getStartTime(), LocalDateTime.of(2024, 4, 7, 12, 30), "Ошибка при добавлении времени");
    }



}