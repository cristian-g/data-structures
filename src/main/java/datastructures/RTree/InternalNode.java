package datastructures.RTree;

import static datastructures.RTree.RTree.ARRAY_SIZE;

public class InternalNode extends Node {
    private double[] start; // Upper left corner
    private double[] end; // Lower right corner
    private Node[] child; // The maximum number of child is defined by the "ARRAY_SIZE" constant

    public InternalNode(Node parent) {
        super(parent);
        this.start = new double[2];
        this.end = new double[2];
        this.child = new Node[ARRAY_SIZE];
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

    public double[] getStart() {
        return start;
    }

    public double[] getEnd() {
        return end;
    }
}
