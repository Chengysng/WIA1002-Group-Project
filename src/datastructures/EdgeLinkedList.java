package datastructures;

public class EdgeLinkedList {
    private class Node {
        Edge data;
        Node next;

        public Node(Edge data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;

    public void add(Edge edge) {
        Node newNode = new Node(edge);
        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    public Node getHead() {
        return head;
    }
}