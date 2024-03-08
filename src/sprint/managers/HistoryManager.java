package sprint.managers;

import sprint.models.Task;
import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();


}
