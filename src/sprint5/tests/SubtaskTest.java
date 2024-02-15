package sprint5.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint5.managers.Managers;
import sprint5.managers.TaskManager;
import sprint5.models.Epic;
import sprint5.models.Status;
import sprint5.models.Subtask;
import sprint5.models.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    private TaskManager taskManager;

    @BeforeEach
    void BeforeEach() {
        taskManager = (new Managers()).getDefault();
    }

    @Test
    void subtasksShouldBeEqualsIfSameId() {
        Task subtask1 = new Task("testSubtask1", "testSubtask1");
        Task subtask2 = new Task(3, "testSubtask2", "testSubtask2", Status.IN_PROGRESS);
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