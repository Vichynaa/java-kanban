package sprint5.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint5.managers.HistoryManager;
import sprint5.managers.Managers;
import sprint5.managers.TaskManager;
import sprint5.models.Status;
import sprint5.models.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    TaskManager taskManager;
    HistoryManager historyManager;
    Task task;

    @BeforeEach
    void BeforeEach() {
        taskManager = (new Managers()).getDefault();
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

}