package datastructures.RTree;

import static datastructures.RTree.RTree.MAX_ITEMS;

public class InternalNode extends Node {
    public Node[] child; // The maximum number of child is defined by the "MAX_ITEMS" constant

    public InternalNode(Node parent) {
        super(parent);
        this.child = new Node[MAX_ITEMS];
    }

    public void setChild(Node[] child) {
        this.child = child;
    }

    public void addChild(Node child) {
        if (isFull()) {
            System.out.println("Internal Node Split Not Implemented Yet!");
        } else {
            this.child[length++] = child;
        }
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
