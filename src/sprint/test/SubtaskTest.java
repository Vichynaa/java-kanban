package sprint.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint.managers.InMemoryTaskManager;
import sprint.managers.Managers;
import sprint.managers.TaskManager;
import sprint.models.Epic;
import sprint.models.Status;
import sprint.models.Subtask;
import sprint.models.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        InMemoryTaskManager.setId(0);
        taskManager = (new Managers()).getDefault();
    }

    @Test
    void subtasksShouldBeEqualsIfSameId() {
        Task subtask1 = new Task("testSubtask1", "testSubtask1");
        Task subtask2 = new Task(1, "testSubtask2", "testSubtask2", Status.IN_PROGRESS);
        assertEquals(subtask1, subtask2);
    }

    @Test
    void createNewSubtask() {
        Epic epic = new Epic("Test epic", "Test epic description");
        Subtask subtask = new Subtask(1,"Test addNewSubtask", "Test addNewSubtask description");
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        final Subtask savedsubtask = taskManager.getSubtask(subtask.getId());

        assertNotNull(savedsubtask, "Подзадачи не найдена.");
        assertEquals(subtask, savedsubtask, "Подзадачи не совпадают.");

        final ArrayList<Subtask> subtasks = (ArrayList<Subtask>)taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество Подзадачи.");
        assertEquals(subtask, subtasks.getFirst(), "Подзадачи не совпадают.");
        assertEquals(subtask.getId(), epic.getSubtasks().getFirst(), "Подзадачи не совпадают в Эпике");
    }
}