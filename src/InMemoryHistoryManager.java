import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager<T> implements HistoryManager<T> {

    private Map<Integer, Node<T>> nodeMap = new HashMap<>();
    private Node<T> head;
    private Node<T> tail;

    public Node<T> linkLast(T element) {

        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<>(tail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        return tail;
    }

    @Override
    public void add(Integer id, T task) {
        removeNode(id);
        nodeMap.put(id, linkLast(task));
    }

    private void removeNode(Integer id) {
        Node<T> node = nodeMap.remove(id);
        if (node == null) {
            return;
        }
        Node<T> prevNode = node.prev;
        Node<T> nextNode = node.next;
        if (prevNode == null) {
            head = nextNode;
        } else {
            prevNode.next = nextNode;
            node.prev = null;
        }
        if (nextNode == null) {
            tail = prevNode;
        } else {
            nextNode.prev = prevNode;
            node.next = null;
        }
        node.data = null;
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }

    @Override
    public List<T> getHistory() {
        List<T> taskHistory = new ArrayList<>();
        for (Node<T> x = head; x != null; x = x.next) {
            taskHistory.add(x.data);
        }
        return taskHistory;
    }

    private class Node<T> {
        private T data;
        private Node<T> next;
        private Node<T> prev;

        public Node(Node<T> prev, T data, Node<T> next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }

}
