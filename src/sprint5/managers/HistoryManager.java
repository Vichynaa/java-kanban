package sprint5.managers;

import sprint5.models.Task;
import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();


}
