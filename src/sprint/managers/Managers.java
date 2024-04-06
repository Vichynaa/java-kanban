package sprint.managers;

public class Managers {
    public InMemoryTaskManager getDefault() {
        return new FileBackedTaskManager();
    }

    public InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
