package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint.managers.InMemoryTaskManager;
import sprint.models.Epic;
import sprint.models.Status;
import sprint.models.Subtask;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EpicTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        InMemoryTaskManager.setId(0);
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void epicsShouldBeEqualsIfSameId() {
        Epic epic1 = new Epic("testEpic1", "testEpic1");
        Epic epic2 = new Epic(1,"testEpic2", "testEpic2");
        assertEquals(epic2, epic1);
    }

    @Test
    void createNewEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createEpic(epic);

        final Epic savedEpic = taskManager.getEpic(epic.getId());

        assertNotNull(savedEpic, "Эпик не найдена.");
        assertEquals(epic, savedEpic, "Эпик не совпадают.");

        final ArrayList<Epic> epics = (ArrayList<Epic>) taskManager.getEpics();

        assertNotNull(epics, "Эпик не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество Эпиков.");
        assertEquals(epic, epics.getFirst(), "Эпики не совпадают.");
    }

    @Test
    void epicBoundaryCheckAllNew() {
        Epic epic = new Epic("Test taskName", "Test description");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask(1,"addNewSubtask1", "Test addNewSubtask description1");
        Subtask subtask2 = new Subtask(1,"addNewSubtask2", "Test addNewSubtask description2");
        taskManager.createTask(subtask1);
        taskManager.createTask(subtask2);
        assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    void epicBoundaryCheckAllDone() {
        Epic epic = new Epic("Test taskName", "Test description");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask(1, 2, "addNewSubtask1", "Test addNewSubtask description1", Status.DONE);
        Subtask subtask2 = new Subtask(1, 3,"addNewSubtask2", "Test addNewSubtask description2", Status.DONE);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        assertEquals(epic.getStatus(), Status.DONE);
    }

    @Test
    void epicBoundaryCheckNewDone() {
        Epic epic = new Epic("Test taskName", "Test description");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask(1, 2, "addNewSubtask1", "Test addNewSubtask description1", Status.NEW);
        Subtask subtask2 = new Subtask(1, 3,"addNewSubtask2", "Test addNewSubtask description2", Status.DONE);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    void epicBoundaryCheckInProgress() {
        Epic epic = new Epic("Test taskName", "Test description");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask(1, 2, "addNewSubtask1", "Test addNewSubtask description1", Status.IN_PROGRESS);
        Subtask subtask2 = new Subtask(1, 3,"addNewSubtask2", "Test addNewSubtask description2", Status.IN_PROGRESS);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

}