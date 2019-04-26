package datastructures.RTree;

import static datastructures.RTree.RTree.ARRAY_SIZE;

public abstract class Node {
    int length;

    Node() {
        this.length = 0;
    }

    public int getLength() {
        return length;
    }

    public boolean isFull() {
        return length == ARRAY_SIZE;
    }
}
