package sprint4.test;

import sprint4.tasks.*;
public class Tests {
    public void mainTest() {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("task1", "task1");
        taskManager.createTask(task1);

        Epic epic1 = new Epic("epic1", "epic1");
        taskManager.createEpic(epic1);

        Subtask sub1 = new Subtask("sub1", "sub1", 2);
        Subtask sub2 = new Subtask("sub2", "sub2", 2);
        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);


        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());

        Task task2 = new Task("task2", "task2", 1, Status.IN_PROGRESS);
        taskManager.updateTask(task2);

        Subtask sub3 = new Subtask("sub3", "sub3", 3, Status.DONE, 2);
        Subtask sub4 = new Subtask("sub4", "sub4", 4, Status.DONE, 2);

        taskManager.updateSubtask(sub3);
        taskManager.updateSubtask(sub4);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());

        taskManager.rmTask(1);
        taskManager.rmEpic(2);

        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubtasks());
    }
}
