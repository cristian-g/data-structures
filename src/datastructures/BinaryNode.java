package datastructures;

public class BinaryNode {
    private ElementWithIntegerKey element;
    private BinaryNode left, right;

    public BinaryNode(ElementWithIntegerKey element) {
        this.element = element;
    }

    public int getKey() {
        return element.getKey();
    }

    public ElementWithIntegerKey getElement() {
        return element;
    }

    public void setElement(ElementWithIntegerKey element) {
        this.element = element;
    }

    public BinaryNode getLeft() {
        return left;
    }

    public void setLeft(BinaryNode left) {
        this.left = left;
    }

    public BinaryNode getRight() {
        return right;
    }

    public void setRight(BinaryNode right) {
        this.right = right;
    }
}
