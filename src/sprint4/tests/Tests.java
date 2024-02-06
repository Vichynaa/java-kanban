package sprint4.tests;

import sprint4.models.Epic;
import sprint4.models.Status;
import sprint4.models.Subtask;
import sprint4.models.Task;
import sprint4.managers.*;

public class Tests {
    public void mainTest() {
        TaskManager taskManager = new TaskManager();
        test1(taskManager);
        test2(taskManager);
    }

    private static void test1(TaskManager taskManager) {
        Task task1 = new Task("task1", "task1");
        taskManager.createTask(task1);

        Epic epic1 = new Epic("epic1", "epic1");
        taskManager.createEpic(epic1);

        Subtask sub1 = new Subtask(2, "sub1", "sub1");
        Subtask sub2 = new Subtask(2, "sub2", "sub2");
        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        printAllTasks(taskManager);
    }

    private static void test2(TaskManager taskManager) {
        Task task2 = new Task(1, "task2", "task2", Status.IN_PROGRESS);
        taskManager.updateTask(task2);

        Subtask sub3 = new Subtask(2, 3, "sub3", "sub3", Status.DONE);
        Subtask sub4 = new Subtask(2, 4, "sub4", "sub4", Status.DONE);

        taskManager.updateSubtask(sub3);
        taskManager.updateSubtask(sub4);

        printAllTasks(taskManager);

        taskManager.removeTask(1);
        taskManager.removeEpic(2);

        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager taskManager) {
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
    }
}
