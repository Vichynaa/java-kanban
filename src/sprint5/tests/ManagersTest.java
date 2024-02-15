package sprint5.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint5.managers.InMemoryHistoryManager;
import sprint5.managers.InMemoryTaskManager;
import sprint5.managers.Managers;
import sprint5.models.Epic;
import sprint5.models.Status;
import sprint5.models.Subtask;
import sprint5.models.Task;

public class ManagersTest {
    private InMemoryTaskManager taskManager = (new Managers()).getDefault();
    private InMemoryHistoryManager historyManager = (new Managers()).getDefaultHistory();
    private Task task1;
    private Epic epic1;
    private Subtask sub1;

    @BeforeEach
    void BeforeEach() {
        taskManager = (new Managers()).getDefault();
        historyManager = (new Managers()).getDefaultHistory();
        task1 = new Task("task1", "task1");
        epic1 = new Epic("epic1", "epic1");
        sub1 = new Subtask(2, "sub1", "sub1");
    }

    @Test
    void checkManagersClass() {
        Assertions.assertNotNull(taskManager, "Метод getDefault() работает не верно");
        Assertions.assertNotNull(historyManager, "Метод getDefaultHistory() работает не верно");
    }

    @Test
    void checkFunctionsInMemoryTaskManager() {
        taskManager.createTask(task1);
        Assertions.assertEquals(taskManager.getTasks().getFirst(), task1);

        taskManager.createEpic(epic1);
        Assertions.assertEquals(taskManager.getEpics().getFirst(), epic1);

        taskManager.createSubtask(sub1);
        Assertions.assertEquals(taskManager.getSubtasks().getFirst(), sub1);

        Task task2 = new Task(1, "task2", "task2", Status.IN_PROGRESS);
        taskManager.updateTask(task2);
        Assertions.assertEquals(taskManager.getTasks().getFirst(), task1);

        Subtask sub3 = new Subtask(2, 3, "sub3", "sub3", Status.DONE);
        taskManager.updateSubtask(sub3);
        Assertions.assertEquals(taskManager.getSubtasks().getFirst(), sub1);

        Assertions.assertEquals(task2.getStatus(), Status.IN_PROGRESS);
        Assertions.assertEquals(sub3.getStatus(), Status.DONE);
        Assertions.assertEquals(epic1.getStatus(), Status.DONE);

        taskManager.removeTask(1);
        taskManager.removeEpic(2);

        Assertions.assertNull(taskManager.getTask(1));
        Assertions.assertNull(taskManager.getEpic(2));
        Assertions.assertNull(taskManager.getSubtask(3));
    }

    @Test
    void checkFunctionsInMemoryHistoryManager() {
        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.getTask(4);
        taskManager.getEpic(5);
        Assertions.assertEquals(taskManager.getHistory().get(0), task1);
    }
}
