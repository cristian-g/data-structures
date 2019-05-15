package models;

import datastructures.ElementWithStringKey;
import datastructures.LinkedList.LinkedList;

/**
 * This class contains the details of the hashtag.
 *
 * @author Cristian, Ferran, Iscle
 *
 */
public class Hashtag implements ElementWithStringKey {
    private String id;
    private LinkedList<Post> posts;

    public Hashtag(String id) {
        this.id = id;
        this.posts = new LinkedList<>();
    }

    public Hashtag() {
        this.posts = new LinkedList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinkedList<Post> getPosts() {
        return posts;
    }

    public void setPosts(LinkedList<Post> posts) {
        this.posts = posts;
    }

    // Two hashtags are equal if their ids are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hashtag hashtag = (Hashtag) o;
        return id.equals(hashtag.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString() {
        return "Hashtag{" +
                "id='" + id + '\'' +
                '}';
    }

    public String toLittleString() {
        return "Hashtag{" +
                "id='" + id + '\'' +
                '}';
    }

    @Override
    public String getKey() {
        return this.id;
    }
}
