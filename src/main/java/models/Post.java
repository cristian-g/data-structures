package models;

import com.google.gson.annotations.SerializedName;
import controller.InstaSalle;
import datastructures.ElementWithCoordinates;
import datastructures.ElementWithIntegerKey;
import datastructures.HashTable.HashTable;
import utils.DoubleUtilities;
import utils.IntegerUtilities;

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
public class Post implements ElementWithIntegerKey, ElementWithCoordinates {

    // Read data
    private int id;
    @SerializedName("liked_by") private String[] likedByUsernames;
    @SerializedName("published_when") private int publishedWhen;
    @SerializedName("published_by") private String publishedByUsername;
    private double[] location;
    @SerializedName("hashtags") private String[] hashtagIds;

    // Graph
    private transient LinkedList<User> likedBy;
    private transient User publishedBy;
    @SerializedName("hashtag_objects") private transient LinkedList<Hashtag> hashtags;

    // Additional attributes
    private boolean isVisible;

    public Post() {
        this.likedBy = new LinkedList<>();
        this.hashtags = new LinkedList<>();
    }

    public Post(int id, double[] location) {
        this.id = id;
        this.location = location;
        this.likedByUsernames = new String[0];
        this.publishedWhen = 10;
        this.publishedByUsername = "";
        this.hashtagIds = new String[0];
        this.likedBy = new LinkedList<>();
        this.publishedBy = new User();
        this.hashtags = new LinkedList<>();
        this.isVisible = true;
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

    public void fillFromUserInput(HashTable<User> usersByUsername, HashTable<Post> postsById) {

        while (true) {
            System.out.println("ID:");
            int desiredId = Integer.parseInt(InstaSalle.scanner.nextLine());
            Post post = (Post) postsById.get(desiredId);
            if (post != null) {
                System.out.println("Post with ID " + desiredId + " already exists... Please, try again.");
            }
            else {
                this.id = desiredId;
                break;
            }
        }

        System.out.println("N of users that liked this post:");
        int length = Integer.parseInt(InstaSalle.scanner.nextLine());
        this.likedByUsernames = new String[length];

        for (int i = 0; i < length; i++) {
            while (true) {
                System.out.println("Username of user " + (i + 1) + " that liked this post:");
                String desiredUsername = InstaSalle.scanner.nextLine();
                User author = (User) usersByUsername.get(desiredUsername);
                if (author == null) {
                    System.out.println("Username " + desiredUsername + " has not been found... Please, try again.");
                }
                else {
                    this.likedByUsernames[i] = desiredUsername;
                    break;
                }
            }
        }

        System.out.println("Published timestamp:");
        this.publishedWhen = Integer.parseInt(InstaSalle.scanner.nextLine());

        while (true) {
            System.out.println("Username of user that published this post:");
            String desiredUsername = InstaSalle.scanner.nextLine();
            User author = (User) usersByUsername.get(desiredUsername);
            if (author == null) {
                System.out.println("Username " + desiredUsername + " has not been found... Please, try again.");
            }
            else {
                this.publishedByUsername = desiredUsername;
                break;
            }
        }

        this.location = new double[2];

        System.out.println("Latitude:");
        this.location[0] = Double.parseDouble(InstaSalle.scanner.nextLine());

        System.out.println("Longitude:");
        this.location[1] = Double.parseDouble(InstaSalle.scanner.nextLine());

        System.out.println("N of hashtags:");
        int lengthHashtags = Integer.parseInt(InstaSalle.scanner.nextLine());
        this.hashtagIds = new String[lengthHashtags];

        for (int i = 0; i < lengthHashtags; i++) {
            System.out.println("Hashtag " + (i + 1));
            this.hashtagIds[i] = InstaSalle.scanner.nextLine();
        }
    }

    public void setInvisible() {
        this.isVisible = false;
    }

    public void setVisible() {
        this.isVisible = true;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void fillWithRandomInfo() {
        this.fillWithRandomGeographicCoordinates();
        this.fillWithRandomId();
    }

    public void fillWithRandomGeographicCoordinates() {
        this.location = DoubleUtilities.computeRandomGeographicCoordinates();
    }

    public void fillWithRandomId() {
        this.id = IntegerUtilities.computeRandomId();
    }

    public String computeLabel() {
        return "Post " + this.id + ":\nlatitude " + DoubleUtilities.computeFormattedGeographicCoordinate(this.location[0]) + "\nlongitude " + DoubleUtilities.computeFormattedGeographicCoordinate(this.location[1]);
    }
}
