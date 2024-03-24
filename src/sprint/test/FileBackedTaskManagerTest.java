<<<<<<< HEAD
=======
import org.junit.jupiter.api.Assertions;
>>>>>>> 06f723c81fbff3298dca296194359be31bffff2c
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint.exceptions.ManagerSaveException;
import sprint.managers.FileBackedTaskManager;
<<<<<<< HEAD
=======
import sprint.managers.Managers;
>>>>>>> 06f723c81fbff3298dca296194359be31bffff2c
import sprint.models.Epic;
import sprint.models.Status;
import sprint.models.Subtask;
import sprint.models.Task;

<<<<<<< HEAD
import java.io.IOException;
=======
>>>>>>> 06f723c81fbff3298dca296194359be31bffff2c
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager.setId(0);
        taskManager = new FileBackedTaskManager();
    }

    @Test
    public void testSaveEmptyFile() {
        try {
            taskManager.save();
            Path fileDataPath = Paths.get("data.txt");
            Path fileHistoryPath = Paths.get("history.txt");

            assertEquals(Files.readString(fileDataPath), "id,type,name,status,description,epic\n", "Файлы data.txt должен создаваться после сохранения изменений");
            assertEquals(Files.readString(fileHistoryPath), "id,type,name,status,description,epic\n", "Файлы historyFile.txt должен создаваться после сохранения изменений");
            Files.delete(fileDataPath);
            Files.delete(fileHistoryPath);
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Test
    void checkFunctionsOfSaveAndLoadFile() {
        Task task1 = new Task("1", "1");
        Epic epic1 = new Epic("2", "2");
        Subtask sub1 = new Subtask(2, "3", "3");

        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(sub1);

        taskManager.getTask(1);

        Path fileDataPath = Paths.get("data.txt");
        Path fileHistoryPath = Paths.get("history.txt");
        String checkFileData = "id,type,name,status,description,epic\n1,TASK,1,NEW,1\n2,EPIC,2,NEW,2\n3,SUBTASK,3,NEW,3,2\n";
        String checkFileHistory = "id,type,name,status,description,epic\n1,TASK,1,NEW,1\n";
        try {
            assertEquals(Files.readString(fileDataPath), checkFileData, "Некорректное сохранение файла data.txt");
            assertEquals(Files.readString(fileHistoryPath), checkFileHistory, "Некорректное сохранение файла history.txt");
            FileBackedTaskManager loadCheck = FileBackedTaskManager.loadFromFile(fileDataPath.toFile());
            assertEquals(loadCheck.getTask(1), new Task(1, "1", "1", Status.NEW));
            assertEquals(loadCheck.getHistory().getFirst(), new Task(1, "1", "1", Status.NEW));
            Files.delete(fileDataPath);
            Files.delete(fileHistoryPath);
        }
        catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }
}
