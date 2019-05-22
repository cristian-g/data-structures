package datastructures.HashTable;

import com.sangupta.murmur.Murmur3;
import datastructures.ElementWithIntegerKey;
import datastructures.ElementWithStringKey;
import datastructures.LinkedList.LinkedList;

import java.nio.charset.StandardCharsets;

import static java.lang.Math.toIntExact;

public class HashTable<E> {

    public static String DATA_STRUCTURE_NAME = "Hash table";

    //public static int DEFAULT_SIZE = 20749;
    public static int DEFAULT_SIZE = 20;// Use this size just in case you want to export image of hashtable
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
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        return Math.abs(Murmur3.hash_x86_32(bytes, key.length(), 0));
    }

    private long hash(int key) {
        return key;
    }

    private long hashElement(E element) {
        if (element instanceof ElementWithStringKey) {
            ElementWithStringKey elementWithStringKey = (ElementWithStringKey) element;
            String key = elementWithStringKey.getKey();
            return this.hash(key);
        }
        ElementWithIntegerKey elementWithIntegerKey = (ElementWithIntegerKey) element;
        return this.hash(elementWithIntegerKey.getKey());
    }

    public void insert(E element) {
        long hash = this.hashElement(element) % this.array.length;
        int hashInt = toIntExact(hash);
        this.array[hashInt].add(element);
    }

    public E get(String key) {
        long hash = this.hash(key) % this.array.length;
        int hashInt = toIntExact(hash);
        return this.array[hashInt].getByStringKey(key);
    }

    public E get(int key) {
        long hash = this.hash(key) % this.array.length;
        int hashInt = toIntExact(hash);
        return this.array[hashInt].getByIntegerKey(key);
    }

    public void remove(String key) {
        long hash = this.hash(key) % this.array.length;
        int hashInt = toIntExact(hash);
        this.array[hashInt].removeByStringKey(key);
    }

    public void addOn(int position, E element) {
        this.array[position].add(element);
    }
}
