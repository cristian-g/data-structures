package test;

import datastructures.ElementWithIntegerKey;

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

    @Override
    public int compareTo(Object o) {
        SimpleElementWithIntegerKey element = (SimpleElementWithIntegerKey) o;
        return Integer.compare(this.getKey(), element.getKey());
    }
}
