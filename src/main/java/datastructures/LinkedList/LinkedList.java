package datastructures.LinkedList;

import datastructures.ElementWithIntegerKey;

public class LinkedList<E> {

    public static String DATA_STRUCTURE_NAME = "Linked list";
    private Node first;
    private Node last;
    private int size;

    public LinkedList() {
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    public Node getFirst() {
        return first;
    }

    public Node getLast() {
        return last;
    }

    public void insert(E e) {
        final Node<E> l = this.last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;

        // If the Linked List is empty, then the new node will be the first
        if (l == null) {
            first = newNode;
        }
        else {
            // Insert the new node as last node
            l.setNext(newNode);
        }

        this.size++;
    }

    public void print() {

        Node currentNode = this.first;

        while (currentNode != null) {
            System.out.println(currentNode.getItem());
            currentNode = currentNode.getNext();// Next node
        }
    }

    public boolean removeByKey(int key) {
        for (Node<E> x = first; x != null; x = x.getNext()) {
            ElementWithIntegerKey elementWithIntegerKey = (ElementWithIntegerKey) x.getItem();
            if (elementWithIntegerKey.getKey() == key) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    E unlink(Node<E> x) {
        final E element = x.getItem();
        final Node<E> next = x.getNext();
        final Node<E> prev = x.getPrev();

        if (prev == null) {
            first = next;
        } else {
            prev.setNext(next);
            x.setPrev(null);
        }

        if (next == null) {
            last = prev;
        } else {
            next.setPrev(prev);
            x.setNext(null);
        }

        x.setItem(null);
        size--;
        return element;
    }

    public boolean contains(int key) {

        // Check if first node contains the key we are searching
        Node currentNode = this.first;
        if (currentNode != null) {
            ElementWithIntegerKey elementWithIntegerKey = (ElementWithIntegerKey) currentNode.getItem();
            if (elementWithIntegerKey.getKey() == key) {
                return true;
            }
        }

        // Search the key in the next nodes
        while (true) {
            if (currentNode != null) {
                ElementWithIntegerKey elementWithIntegerKey = (ElementWithIntegerKey) currentNode.getItem();
                if (elementWithIntegerKey.getKey() != key) {
                    currentNode = currentNode.getNext();
                }
                else {
                    break;
                }
            }
            else {
                break;
            }
        }

        // Check if the key has been found
        if (currentNode != null) {
            return true;
        }

        // Key has not been found
        return false;
    }

    public E getByKey(int key) {

        // Check if first node contains the key we are searching
        Node currentNode = this.first;
        if (currentNode != null) {
            ElementWithIntegerKey elementWithIntegerKey = (ElementWithIntegerKey) currentNode.getItem();
            if (elementWithIntegerKey.getKey() == key) {
                return (E) currentNode.getItem();
            }
        }

        // Search the key in the next nodes
        while (true) {
            if (currentNode != null) {
                ElementWithIntegerKey elementWithIntegerKey = (ElementWithIntegerKey) currentNode.getItem();
                if (elementWithIntegerKey.getKey() != key) {
                    currentNode = currentNode.getNext();
                }
                else {
                    break;
                }
            }
            else {
                break;
            }
        }

        // Check if the key has been found
        if (currentNode != null) {
            return (E) currentNode.getItem();
        }

        // Key has not been found
        return null;
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = this.first; x != null; x = x.getNext())
            result[i++] = x.getItem();
        return result;
    }

    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[])java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        int i = 0;
        Object[] result = a;
        for (Node<E> x = first; x != null; x = x.getNext())
            result[i++] = x.getItem();

        if (a.length > size)
            a[size] = null;

        return a;
    }
}