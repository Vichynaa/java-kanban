package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint.managers.FileBackedTaskManager;
import sprint.managers.InMemoryTaskManager;
import sprint.models.Epic;
import sprint.models.Status;
import sprint.models.Subtask;
import sprint.models.Task;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        InMemoryTaskManager.setId(0);
        taskManager = new FileBackedTaskManager();
    }

    @Test
    public void testSaveEmptyFile() {
        Assertions.assertDoesNotThrow(() -> {
            taskManager.save();
            Path fileDataPath = Paths.get("data.txt");
            Path fileHistoryPath = Paths.get("history.txt");

            assertEquals(Files.readString(fileDataPath), "id,type,name,status,description,epic,duration,startTime\n", "Файлы data.txt должен создаваться после сохранения изменений");
            assertEquals(Files.readString(fileHistoryPath), "id,type,name,status,description,epic,duration,startTime\n", "Файлы historyFile.txt должен создаваться после сохранения изменений");
            Files.delete(fileDataPath);
            Files.delete(fileHistoryPath);}, "Ошибка при сохранении файла");
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
        taskManager.save();

        Path fileDataPath = Paths.get("data.txt");
        Path fileHistoryPath = Paths.get("history.txt");
        String checkFileData = "id,type,name,status,description,epic,duration,startTime\n1,TASK,1,NEW,1\n2,EPIC,2,NEW,2\n3,SUBTASK,3,NEW,3,2\n";
        String checkFileHistory = "id,type,name,status,description,epic,duration,startTime\n1,TASK,1,NEW,1\n";
        Assertions.assertDoesNotThrow(() -> {
                    assertEquals(Files.readString(fileDataPath), checkFileData, "Некорректное сохранение файла data.txt");
                    assertEquals(Files.readString(fileHistoryPath), checkFileHistory, "Некорректное сохранение файла history.txt");
                    FileBackedTaskManager loadCheck = FileBackedTaskManager.loadFromFile(fileDataPath.toFile());
                    assertEquals(loadCheck.getTask(1), new Task(1, "1", "1", Status.NEW));
                    assertEquals(loadCheck.getHistory().getFirst(), new Task(1, "1", "1", Status.NEW));
                    Files.delete(fileDataPath);
                    Files.delete(fileHistoryPath);
                }, "Ошибка в тесте checkFunctionsOfSaveAndLoadFile");
    }

    @Test
    void checkFunctionsOfSaveAndLoadFileWithTime() {
        Task task1 = new Task("1", "1", 1, LocalDateTime.of(2024, 1, 1, 1, 1));

        taskManager.createTask(task1);
        taskManager.getTask(1);
        taskManager.save();

        Path fileDataPath = Paths.get("data.txt");
        Path fileHistoryPath = Paths.get("history.txt");
        String checkFileData = "id,type,name,status,description,epic,duration,startTime\n1,TASK,1,NEW,1,PT1M,2024-01-01T01:01\n";
        String checkFileHistory = "id,type,name,status,description,epic,duration,startTime\n1,TASK,1,NEW,1,PT1M,2024-01-01T01:01\n";
        Assertions.assertDoesNotThrow(() -> {
            assertEquals(Files.readString(fileDataPath), checkFileData, "Некорректное сохранение файла data.txt");
            assertEquals(Files.readString(fileHistoryPath), checkFileHistory, "Некорректное сохранение файла history.txt");
            FileBackedTaskManager loadCheck = FileBackedTaskManager.loadFromFile(fileDataPath.toFile());
            assertEquals(loadCheck.getTask(1).getStartTime(), LocalDateTime.of(2024, 1, 1, 1, 1), "Ошибка при загрузке из файла");
            assertEquals(loadCheck.getTask(1).getDuration(), Duration.ofMinutes(1), "Ошибка при загрузке из файла");
            Files.delete(fileDataPath);
            Files.delete(fileHistoryPath);
        }, "Ошибка в тесте checkFunctionsOfSaveAndLoadFile");
    }
}
