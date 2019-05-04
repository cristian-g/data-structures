package datastructures.LinkedList;

import datastructures.ElementWithIntegerKey;

public class LinkedList<E> {

    public static String DATA_STRUCTURE_NAME = "Linked list";
    private Node first;
    private Node last;
    private int size;

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

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
            l.next = newNode;
        }

        this.size++;
    }

    public void print() {

        Node currentNode = this.first;

        while (currentNode != null) {
            System.out.println(currentNode.item);
            currentNode = currentNode.next;// Next node
        }
    }

    public boolean removeByKey(int key) {
        for (Node<E> x = first; x != null; x = x.next) {
            ElementWithIntegerKey elementWithIntegerKey = (ElementWithIntegerKey) x.item;
            if (elementWithIntegerKey.getKey() == key) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    E unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        return element;
    }

    public boolean contains(int key) {

        // Check if first node contains the key we are searching
        Node currentNode = this.first;
        if (currentNode != null) {
            ElementWithIntegerKey elementWithIntegerKey = (ElementWithIntegerKey) currentNode.item;
            if (elementWithIntegerKey.getKey() == key) {
                return true;
            }
        }

        // Search the key in the next nodes
        while (true) {
            if (currentNode != null) {
                ElementWithIntegerKey elementWithIntegerKey = (ElementWithIntegerKey) currentNode.item;
                if (elementWithIntegerKey.getKey() != key) {
                    currentNode = currentNode.next;
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
            ElementWithIntegerKey elementWithIntegerKey = (ElementWithIntegerKey) currentNode.item;
            if (elementWithIntegerKey.getKey() == key) {
                return (E) currentNode.item;
            }
        }

        // Search the key in the next nodes
        while (true) {
            if (currentNode != null) {
                ElementWithIntegerKey elementWithIntegerKey = (ElementWithIntegerKey) currentNode.item;
                if (elementWithIntegerKey.getKey() != key) {
                    currentNode = currentNode.next;
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
            return (E) currentNode.item;
        }

        // Key has not been found
        return null;
    }

    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Node<E> x = this.first; x != null; x = x.next)
            result[i++] = x.item;
        return result;
    }

    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[])java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), size);
        int i = 0;
        Object[] result = a;
        for (Node<E> x = first; x != null; x = x.next)
            result[i++] = x.item;

        if (a.length > size)
            a[size] = null;

        return a;
    }
}