package sprint.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LinkedHashMapHandMade {
    private final Map<Integer, Node> mapHistory = new HashMap<>();
    private Node head;
    private Node tail;

    public void linkLast(Task value) {
        Node node = new Node(value.getId(), value);
        if (mapHistory.containsKey(value.getId())) {
            removeNode(node);
        }
        mapHistory.put(node.getKey(), node);
        if (head == null) {
            head = node;
        } else {
            tail.setNext(node);
            node.setPrev(tail);
        }
        tail = node;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node valueNode = head;
        boolean headContain = valueNode != null;
        while (headContain) {
            tasks.add(valueNode.getValue());
            valueNode = valueNode.getNext();
            headContain = valueNode != null;
        }
        return tasks;
    }

    public void removeNode(Node nodeToRemove) {
        Node node = mapHistory.get(nodeToRemove.getKey());
        if (node != null && node.equals(nodeToRemove)) {
            mapHistory.remove(node.getKey());
            if (node == head && node == tail) {
                head = null;
                tail = null;
            } else if (node == head) {
                head = head.getNext();
                head.setPrev(null);
            } else if (node == tail) {
                tail = tail.getPrev();
                tail.setPrev(null);
            } else {
                node.getPrev().setNext(node.getNext());
                node.getNext().setPrev(node.getPrev());
            }
        }
    }
}