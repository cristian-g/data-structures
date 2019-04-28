package datastructures.LinkedList;

import datastructures.ElementWithIntegerKey;

public class LinkedList {

    private Node head;
    private Node last;
    private int size;

    private class Node {

        private ElementWithIntegerKey data;
        private Node next;

        Node(ElementWithIntegerKey data) {
            this.data = data;
            this.next = null;
        }
    }

    public LinkedList() {
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    public void insert(ElementWithIntegerKey data) {

        Node newNode = new Node(data);

        // If the Linked List is empty, then the new node will be the head
        if (this.head == null) {
            this.head = newNode;
            this.last = newNode;
        }
        else {
            // Insert the new node as last node
            this.last.next = newNode;
            this.last = newNode;
        }

        this.size++;
    }

    public void print() {

        Node currentNode = this.head;

        while (currentNode != null) {
            System.out.println(currentNode.data);
            currentNode = currentNode.next;// Next node
        }
    }

    public ElementWithIntegerKey deleteByKey(int key) {

        // Check if head node contains the key we want to delete
        Node currentNode = this.head;
        if (currentNode != null && currentNode.data.getKey() == key) {
            // Delete currentNode
            this.head = currentNode.next;
            this.size--;
            return currentNode.data;
        }

        // Search the key in the next nodes
        Node previousNode = null;
        while (currentNode != null && currentNode.data.getKey() != key) {
            previousNode = currentNode;
            currentNode = currentNode.next;
        }

        // Check if the key has been found
        if (currentNode != null) {
            // Delete currentNode
            previousNode.next = currentNode.next;
            this.size--;
            return currentNode.data;
        }

        // Key has not been found
        // Return null
        return null;
    }

    public boolean contains(int key) {

        // Check if head node contains the key we are searching
        Node currentNode = this.head;
        if (currentNode != null && currentNode.data.getKey() == key) {
            return true;
        }

        // Search the key in the next nodes
        while (currentNode != null && currentNode.data.getKey() != key) {
            currentNode = currentNode.next;
        }

        // Check if the key has been found
        if (currentNode != null) {
            return true;

        }

        // Key has not been found
        return false;
    }

    public ElementWithIntegerKey[] toArray() {
        ElementWithIntegerKey[] array = new ElementWithIntegerKey[this.size];
        Node currentNode = this.head;
        for (int i = 0; i < this.size; i++) {
            array[i] = currentNode.data;
            currentNode = currentNode.next;
        }
        return array;
    }
}