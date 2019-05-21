package test;

import datastructures.ElementWithStringKey;

public class SimpleElementWithStringKey implements ElementWithStringKey {
    private String id;

    public SimpleElementWithStringKey(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getKey() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Two pots are equal if their ids are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleElementWithStringKey element = (SimpleElementWithStringKey) o;
        return id.equals(element.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return "Element {" + '\n' +
                "id='" + id + ", " + '\n' +
                '}';
    }
}
