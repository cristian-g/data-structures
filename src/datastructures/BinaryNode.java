package datastructures;

import utils.print.PrintableNode;

public class BinaryNode implements PrintableNode {
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

    @Override
    public PrintableNode[] getConnections() {

        PrintableNode[] printableNodes = null;

        if (this.getLeft() != null && this.getRight() != null)
            printableNodes = new PrintableNode[]{this.getLeft(), this.getRight()};
        else if (this.getLeft() != null && this.getRight() == null)
            printableNodes = new PrintableNode[]{this.getLeft(), null};
        else if (this.getLeft() == null && this.getRight() != null)
            printableNodes = new PrintableNode[]{null, this.getRight()};
        else if (this.getLeft() == null && this.getRight() == null)
            printableNodes = new PrintableNode[]{null, null};

        return printableNodes;
    }

    @Override
    public String getPrintName() {
        return "" + this.getKey();
    }
}
