package models;

import com.google.gson.annotations.SerializedName;

/**
 * This class contains the details of the user.
 * Note that annotations (@SerializedName) can be used toId respect lowerCamelCase notation in Java.
 *
 * @see com.google.gson.annotations
 *
 * @author Cristian, Ferran, Iscle
 *
 */
public class Post {
    private int id;

    @SerializedName("liked_by")
    private String[] likedByUsernames;

    @SerializedName("published_when")
    private int publishedWhen;

    @SerializedName("published_by")
    private String publishedBy;

    private double[] location;
    private String[] hashtags;

    public Post(int id, String[] likedByUsernames, int publishedWhen, String publishedBy, double[] location, String[] hashtags) {
        this.id = id;
        this.likedByUsernames = likedByUsernames;
        this.publishedWhen = publishedWhen;
        this.publishedBy = publishedBy;
        this.location = location;
        this.hashtags = hashtags;
    }

    public int getId() {
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

    public int getPublishedWhen() {
        return publishedWhen;
    }

    public void setPublishedWhen(int publishedWhen) {
        this.publishedWhen = publishedWhen;
    }

    public String getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public String[] getHashtags() {
        return hashtags;
    }

    public void setHashtags(String[] hashtags) {
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
        return "Post{" +
                "id='" + id + '\'' +
                ", published_by=" + publishedBy +
                '}';
    }
}
