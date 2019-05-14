package datastructures.HashTable;

import datastructures.ElementWithIntegerKey;
import datastructures.LinkedList.LinkedList;

public class HashTable {
    public static int DEFAULT_SIZE = 30;
    private LinkedList<ElementWithIntegerKey>[] array;

    public HashTable() {
        this.array = new LinkedList[DEFAULT_SIZE];
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            this.array[i] = new LinkedList<>();
        }
    }

    public LinkedList<ElementWithIntegerKey>[] getArray() {
        return array;
    }

    public void setArray(LinkedList<ElementWithIntegerKey>[] array) {
        this.array = array;
    }

    public void addOn(int position, ElementWithIntegerKey element) {
        this.array[position].insert(element);
    }
}
