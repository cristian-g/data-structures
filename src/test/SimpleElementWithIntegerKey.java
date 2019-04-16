package test;

import com.google.gson.annotations.SerializedName;
import datastructures.ElementWithIntegerKey;
import models.Hashtag;

public class SimpleElementWithIntegerKey implements ElementWithIntegerKey, Comparable {
    private int id;

    public SimpleElementWithIntegerKey(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public int getKey() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Two pots are equal if their ids are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleElementWithIntegerKey element = (SimpleElementWithIntegerKey) o;
        return id == element.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Element {" + '\n' +
                "id='" + id + ", " + '\n' +
                '}';
    }

    public static String arrayToString(ElementWithIntegerKey[] array) {
        StringBuilder sb = new StringBuilder();
        for (ElementWithIntegerKey element: array) {
            sb.append('\n');
            sb.append(element.toString());
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Object o) {
        SimpleElementWithIntegerKey element = (SimpleElementWithIntegerKey) o;
        return Integer.compare(this.getKey(), element.getKey());
    }
}
