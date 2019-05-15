package datastructures.HashTable;

import datastructures.ElementWithStringKey;
import datastructures.LinkedList.LinkedList;

public class HashTable<E> {
    public static int DEFAULT_SIZE = 30;
    private LinkedList<E>[] array;

    public HashTable() {
        this.array = new LinkedList[DEFAULT_SIZE];
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            this.array[i] = new LinkedList<>();
        }
    }

    public LinkedList<E>[] getArray() {
        return array;
    }

    public void setArray(LinkedList<E>[] array) {
        this.array = array;
    }

    private long hash(String key) {
        // TODO
        return 0;
    }

    private long hash(int key) {
        // TODO
        return 0;
    }

    private long hashElement(E element) {
        ElementWithStringKey elementWithStringKey = (ElementWithStringKey) element;
        return this.hash(elementWithStringKey.getKey());
    }

    public void insert(E element) {
        long hash = this.hashElement(element);
        int hashInt = (int) hash;
        this.array[hashInt].add(element);
    }

    public E get(String key) {
        long hash = this.hash(key);
        int hashInt = (int) hash;
        return this.array[hashInt].getByStringKey(key);
    }

    public void remove(String key) {
        long hash = this.hash(key);
        int hashInt = (int) hash;
        this.array[hashInt].removeByStringKey(key);
    }

    public void addOn(int position, E element) {
        this.array[position].add(element);
    }
}
