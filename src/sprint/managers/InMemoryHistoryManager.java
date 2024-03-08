package sprint.managers;

import sprint.models.LinkedHashMapHandMade;
import sprint.models.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    private final LinkedHashMapHandMade history = new LinkedHashMapHandMade();

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history.getTasks());
    }

    @Override
    public void add(Task object) {
        history.linkLast(object);
    }
}



