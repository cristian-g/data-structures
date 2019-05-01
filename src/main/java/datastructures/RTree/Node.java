package datastructures.RTree;

import static datastructures.RTree.RTree.ARRAY_SIZE;

public abstract class Node {
    Node parent;
    int length;

    Node(Node parent) {
        this.parent = parent;
        this.length = 0;
    }

    public Node getParent() {
        return parent;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public boolean isFull() {
        return length == ARRAY_SIZE;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
