package sprint5.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint5.managers.InMemoryTaskManager;
import sprint5.managers.Managers;
import sprint5.models.Epic;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EpicTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    void BeforeEach() {
        taskManager = (new Managers()).getDefault();
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
}