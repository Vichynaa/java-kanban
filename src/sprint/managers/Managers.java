package sprint.managers;

public class Managers {
    public InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
