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
public class User {
    private String username;
    private int creation;

    @SerializedName("to_follow")
    private String[] toFollowUsernames;

    public User(String username, int creation, String[] toFollowUsernames) {
        this.username = username;
        this.creation = creation;
        this.toFollowUsernames = toFollowUsernames;
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
        return "User{" +
                "username='" + username + '\'' +
                ", creation=" + creation +
                '}';
    }
}
