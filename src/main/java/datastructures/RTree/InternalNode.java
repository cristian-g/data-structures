package datastructures.RTree;

import static datastructures.RTree.RTree.ARRAY_SIZE;

public class InternalNode extends Node {
    private Node[] child; // The maximum number of child is defined by the "ARRAY_SIZE" constant

    public InternalNode(Node parent) {
        super(parent);
        this.child = new Node[ARRAY_SIZE];
    }

    public void setChild(Node[] child) {
        this.child = child;
    }

    // TODO: implement this function if we need it
    public void addNode(Node node) {

    }

    // TODO: implement this function if we need it
    public Node getNode(int position) {
        return null;
    }

    // TODO: implement this function if we need it
    public void removeNode(int position) {

    }

    public Node[] getChild() {
        return child;
    }
}
