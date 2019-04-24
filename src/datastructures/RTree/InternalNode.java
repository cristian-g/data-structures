package datastructures.RTree;

import models.Point;

import static datastructures.RTree.RTree.ARRAY_SIZE;

public class InternalNode extends Node {
    private Point[] corners; // Upper left and lower right corners go here
    private Node[] child; // The maximum number of child is defined by the "ARRAY_SIZE" constant

    public InternalNode() {
        this.corners = new Point[2];
        this.child = new Node[ARRAY_SIZE];
    }

    // TODO: implement this function if we need it
    public void addNode(Node node, int position) {

    }

    // TODO: implement this function if we need it
    public Node getNode(int position) {
        return null;
    }

    // TODO: implement this function if we need it
    public void removeNode(int position) {

    }
}
