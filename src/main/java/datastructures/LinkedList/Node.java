package datastructures.LinkedList;

import models.Hashtag;
import utils.print.PrintableNode;

public class Node<E> implements PrintableNode {
    private E item;
    private Node<E> next;
    private Node<E> prev;

    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }

    public E getItem() {
        return item;
    }

    public void setItem(E item) {
        this.item = item;
    }

    public Node<E> getNext() {
        return next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public Node<E> getPrev() {
        return prev;
    }

    public void setPrev(Node<E> prev) {
        this.prev = prev;
    }

    @Override
    public PrintableNode[] getConnections() {
        PrintableNode[] printableNodes = new PrintableNode[]{this.next};
        return printableNodes;
    }

    @Override
    public String getPrintName() {
        if (this.item instanceof Hashtag) {
            Hashtag hashtag = (Hashtag) this.item;
            return hashtag.getId();
        }
        else {
            return "Node";
        }
    }
}