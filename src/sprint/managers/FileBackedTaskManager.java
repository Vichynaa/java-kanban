package sprint.managers;

import sprint.exceptions.ManagerSaveException;
import sprint.models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String DATA_PATH = "data.txt";
    private static final String HISTORY_PATH = "history.txt";


    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task superTask = super.getTask(id);
        save();
        return superTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic superEpic = super.getEpic(id);
        save();
        return superEpic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask superSubtask = super.getSubtask(id);
        save();
        return superSubtask;
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    public void save() {
        newFile();
        String csv = "id,type,name,status,description,epic\n";
        try (BufferedWriter dataWriter = new BufferedWriter(new FileWriter(DATA_PATH, StandardCharsets.UTF_8))) {
            dataWriter.write(csv);
            for (Task task : super.tasks.values()) {
                dataWriter.write(task.csvString() + "\n");
            }
            for (Epic epic : super.epics.values()) {
                dataWriter.write(epic.csvString() + "\n");
            }
            for (Subtask subtask : super.subtasks.values()) {
                dataWriter.write(subtask.csvString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка - сохранения файла data.txt");
        }

        try (BufferedWriter historyWriter = new BufferedWriter(new FileWriter(HISTORY_PATH, StandardCharsets.UTF_8))) {
            historyWriter.write(csv);
            for (Task task : super.history.getHistory()) {
                historyWriter.write(task.csvString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка - сохранения файла history.txt");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        try {
            String tasks = Files.readString(file.toPath());
            for (int i = 1; i < tasks.split("\n").length; i++) {
                switch (tasks.split("\n")[i].split(",")[1]) {
                    case "TASK":
                        fileBackedTaskManager.createTask(fileBackedTaskManager.fromString(tasks.split("\n")[i]));
                        break;
                    case "EPIC":
                        fileBackedTaskManager.createEpic((Epic) fileBackedTaskManager.fromString(tasks.split("\n")[i]));
                        break;
                    case "SUBTASK":
                        fileBackedTaskManager.createSubtask((Subtask) fileBackedTaskManager.fromString(tasks.split("\n")[i]));
                        break;
                }
            }
            for (Integer id : historyFromString(Files.readString(new File(HISTORY_PATH).toPath()))) {
                if (fileBackedTaskManager.tasks.containsKey(id)) {
                    fileBackedTaskManager.history.add(fileBackedTaskManager.getTask(id));
                } else if (fileBackedTaskManager.epics.containsKey(id)) {
                    fileBackedTaskManager.history.add(fileBackedTaskManager.getEpic(id));
                } else {
                    fileBackedTaskManager.history.add(fileBackedTaskManager.getSubtask(id));
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке данных");
        }
        return fileBackedTaskManager;
    }

    private void newFile() {
        Path path = Paths.get(DATA_PATH);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка создания файла");
        }
    }

    private Task fromString(String value) {
        String[] values = value.split(",");
        switch (values[1]) {
            case "TASK":
                return new Task(Integer.parseInt(values[0]), values[2], values[4], Status.valueOf(values[3]));
            case "EPIC":
                return new Epic(Integer.parseInt(values[0]), values[2], values[4], Status.valueOf(values[3]));
        }
        return new Subtask(Integer.parseInt(values[5]), Integer.parseInt(values[0]), values[2], values[4], Status.valueOf(values[3]));
    }

    private static List<Integer> historyFromString(String value) {
        String[] values = value.split("\n");
        ArrayList<Integer> historyId = new ArrayList<>();
        for (int i = 1; i < values.length; i++) {
            historyId.add(Integer.parseInt(values[i].split(",")[0]));
        }
        return historyId;
    }


}
