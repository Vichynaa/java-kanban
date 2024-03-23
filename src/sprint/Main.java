package sprint;

import sprint.managers.FileBackedTaskManager;

import java.io.File;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(Paths.get("data.txt").toFile());
        System.out.println(fileBackedTaskManager.getTasks());
    }
}
