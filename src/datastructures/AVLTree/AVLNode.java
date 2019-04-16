package datastructures.AVLTree;

import datastructures.BinaryNode;
import datastructures.ElementWithIntegerKey;

public class AVLNode extends BinaryNode {
    private int height;

    public AVLNode(ElementWithIntegerKey element) {
        super(element);
        this.height = 1;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public AVLNode getLeft() {
        return (AVLNode) super.getLeft();
    }

    @Override
    public AVLNode getRight() {
        return (AVLNode) super.getRight();
    }
}
