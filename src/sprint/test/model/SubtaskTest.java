package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint.managers.InMemoryTaskManager;
import sprint.managers.TaskManager;
import sprint.models.Epic;
import sprint.models.Subtask;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubtaskTest {

    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        InMemoryTaskManager.setId(0);
        taskManager = new InMemoryTaskManager();
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