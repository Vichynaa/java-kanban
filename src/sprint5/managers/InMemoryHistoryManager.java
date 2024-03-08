package sprint5.managers;

import sprint5.models.LinkedHashMapHandMade;
import sprint5.models.Task;

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



