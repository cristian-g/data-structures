package models;

import com.google.gson.annotations.SerializedName;
import datastructures.ElementWithIntegerKey;

import java.util.LinkedList;

/**
 * This class contains the details of the user.
 * Note that annotations (@SerializedName) can be used to respect lowerCamelCase notation in Java.
 *
 * @see com.google.gson.annotations
 *
 * @author Cristian, Ferran, Iscle
 *
 */
public class Post implements ElementWithIntegerKey {
    private int id;

    @SerializedName("liked_by")
    private String[] likedByUsernames;

    private LinkedList<User> likedBy;

    @SerializedName("published_when")
    private int publishedWhen;

    @SerializedName("published_by")
    private String publishedByUsername;

    private User publishedBy;

    private double[] location;

    @SerializedName("hashtags")
    private String[] hashtagIds;

    @SerializedName("hashtag_objects")
    private LinkedList<Hashtag> hashtags;

    public Post() {
        this.likedBy = new LinkedList<>();
        this.hashtags = new LinkedList<>();
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

    public String[] getLikedByUsernames() {
        return likedByUsernames;
    }

    public void setLikedByUsernames(String[] likedByUsernames) {
        this.likedByUsernames = likedByUsernames;
    }

    public LinkedList<User> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(LinkedList<User> likedBy) {
        this.likedBy = likedBy;
    }

    public int getPublishedWhen() {
        return publishedWhen;
    }

    public void setPublishedWhen(int publishedWhen) {
        this.publishedWhen = publishedWhen;
    }

    public String getPublishedByUsername() {
        return publishedByUsername;
    }

    public void setPublishedByUsername(String publishedByUsername) {
        this.publishedByUsername = publishedByUsername;
    }

    public User getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(User publishedBy) {
        this.publishedBy = publishedBy;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public String[] getHashtagIds() {
        return hashtagIds;
    }

    public void setHashtagIds(String[] hashtagIds) {
        this.hashtagIds = hashtagIds;
    }

    public LinkedList<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(LinkedList<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    // Two pots are equal if their ids are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public String toString() {

        StringBuilder sb1 = new StringBuilder();
        for (User user: this.getLikedBy()) {
            sb1.append('\n');
            sb1.append(user.toLittleString());
        }

        StringBuilder sb2 = new StringBuilder();
        for (Hashtag hashtag: this.getHashtags()) {
            sb2.append('\n');
            sb2.append(hashtag.toLittleString());
        }

        return "Post {" + '\n' +
                "id='" + id + ", " + '\n' +
                "published_by=" + publishedBy.toLittleString() + ", " + '\n' +
                "liked_by=[" + sb1.toString().replace("\n", "\n\t") + '\n' + "]" + ", " + '\n' +
                "hashtags=[" + sb2.toString().replace("\n", "\n\t") + '\n' + "]" + ", " + '\n' +
                '}';
    }

    public String toLittleString() {
        return "Post {" + '\n' +
                "id='" + id + ", " + '\n' +
                "published_by=" + publishedByUsername + '\n' +
                '}';
    }
}
