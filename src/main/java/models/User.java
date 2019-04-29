package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import controller.InstaSalle;
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
public class User implements ElementWithIntegerKey {
    private String username;
    private int creation;

    @SerializedName("to_follow")
    private String[] toFollowUsernames;

    private LinkedList<User> following;
    private LinkedList<User> followers;

    private LinkedList<Post> posts;
    private LinkedList<Post> likedPosts;

    public User() {
        this.following = new LinkedList<>();
        this.followers = new LinkedList<>();
        this.posts = new LinkedList<>();
        this.likedPosts = new LinkedList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCreation() {
        return creation;
    }

    public void setCreation(int creation) {
        this.creation = creation;
    }

    public String[] getToFollowUsernames() {
        return toFollowUsernames;
    }

    public void setToFollowUsernames(String[] toFollowUsernames) {
        this.toFollowUsernames = toFollowUsernames;
    }

    public LinkedList<User> getFollowing() {
        return following;
    }

    public void setFollowing(LinkedList<User> following) {
        this.following = following;
    }

    public LinkedList<User> getFollowers() {
        return followers;
    }

    public void setFollowers(LinkedList<User> followers) {
        this.followers = followers;
    }

    public LinkedList<Post> getPosts() {
        return posts;
    }

    public void setPosts(LinkedList<Post> posts) {
        this.posts = posts;
    }

    public LinkedList<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(LinkedList<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }

    // Two users are equal if their usernames are equal
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username == user.username;
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public String toString() {

        StringBuilder sb1 = new StringBuilder();
        for (User user: this.getFollowing()) {
            sb1.append('\n');
            sb1.append(user.toLittleString());
        }

        StringBuilder sb2 = new StringBuilder();
        for (User user: this.getFollowers()) {
            sb2.append('\n');
            sb2.append(user.toLittleString());
        }

        StringBuilder sb3 = new StringBuilder();
        for (Post post: this.getPosts()) {
            sb3.append('\n');
            sb3.append(post.toLittleString());
        }

        StringBuilder sb4 = new StringBuilder();
        for (Post post: this.getLikedPosts()) {
            sb4.append('\n');
            sb4.append(post.toLittleString());
        }

        return "User {" + '\n' +
                "username='" + username + ", " + '\n' +
                "creation=" + creation + ", " + '\n' +
                "following=[" + sb1.toString().replace("\n", "\n\t") + '\n' + "]" + ", " + '\n' +
                "followers=[" + sb2.toString().replace("\n", "\n\t") + '\n' + "]" + ", " + '\n' +
                "posts=[" + sb3.toString().replace("\n", "\n\t") + '\n' + "]" + ", " + '\n' +
                "liked_posts=[" + sb4.toString().replace("\n", "\n\t") + '\n' + "]" + ", " + '\n' +
                '}';
    }

    public String toLittleString() {
        return "User {" + '\'' +
                "username='" + username + ", " + '\n' +
                "creation=" + creation + ", " + '\n' +
                '}';
    }

    @Override
    public int getKey() {
        return this.username.hashCode();
    }

    public void fillFromUserInput() {

        System.out.println("Username:");
        this.username = InstaSalle.scanner.nextLine();

        System.out.println("Creation timestamp:");
        this.creation = Integer.parseInt(InstaSalle.scanner.nextLine());

        System.out.println("N of users to follow:");
        int length = Integer.parseInt(InstaSalle.scanner.nextLine());
        this.toFollowUsernames = new String[length];

        for (int i = 0; i < length; i++) {
            System.out.println("Username of user to follow:");
            this.toFollowUsernames[i] = InstaSalle.scanner.nextLine();
        }
    }
}
