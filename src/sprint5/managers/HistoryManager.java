package sprint5.managers;

import sprint5.models.Task;

import java.util.Collection;

public interface HistoryManager {
    void add(Task task);
    Collection<Task> getHistory();
}
