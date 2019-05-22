package datastructures.Graph;

import datastructures.HashTable.HashTable;
import datastructures.LinkedList.LinkedList;
import models.Hashtag;
import models.Post;
import models.User;

public class Graph {

    public static String DATA_STRUCTURE_NAME = "Graph";

    // Hash tables
    private HashTable<User> usersByUsername;
    private HashTable<Hashtag> hashtagsByName;

    public Graph() {
        this.usersByUsername = new HashTable<>();
        this.hashtagsByName = new HashTable<>();
    }

    public Graph(HashTable<User> usersByUsername, HashTable<Hashtag> hashtagsByName) {
        this.usersByUsername = usersByUsername;
        this.hashtagsByName = hashtagsByName;
    }

    public HashTable<User> getUsersByUsername() {
        return usersByUsername;
    }

    public void setUsersByUsername(HashTable<User> usersByUsername) {
        this.usersByUsername = usersByUsername;
    }

    public HashTable<Hashtag> getHashtagsByName() {
        return hashtagsByName;
    }

    public void setHashtagsByName(HashTable<Hashtag> hashtagsByName) {
        this.hashtagsByName = hashtagsByName;
    }

    public void computeInitialGraph(User[] users, Post[] posts) {

        // Compute graph for each user
        for (User user: users) {
            this.computeGraph(user);
        }

        // Compute graph for each post
        for (Post post: posts) {
            this.computeGraph(post);
        }
    }

    public void computeGraph(User user) {
        // Connect following users
        for (String usernameToFollow: user.getToFollowUsernames()) {
            User toFollow = (User) usersByUsername.get(usernameToFollow);
            user.getFollowing().add(toFollow);
            toFollow.getFollowers().add(user);
        }
    }

    public void removeFromGraph(User user) {
        // Disconnect follower users
        LinkedList<User> linkedList = user.getFollowers();
        User[] array = linkedList.toArray(new User[linkedList.getSize()]);
        for (User follower: array) {
            follower.getFollowing().removeByStringKey(user.getKey());
        }
    }

    public void computeGraph(Post post) {

        // Post liked by...
        for (String usernameWhoLiked: post.getLikedByUsernames()) {
            User likedBy = (User) usersByUsername.get(usernameWhoLiked);
            post.getLikedBy().add(likedBy);
            likedBy.getLikedPosts().add(post);
        }

        // Post published by...
        User author = (User) usersByUsername.get(post.getPublishedByUsername());
        author.getPosts().add(post);
        post.setPublishedBy(author);

        // Post with hashtags...
        for (String hashtagId: post.getHashtagIds()) {
            Hashtag hashtag = (Hashtag) hashtagsByName.get(hashtagId);
            if (hashtag == null) {
                hashtag = new Hashtag(hashtagId);
                hashtagsByName.insert(hashtag);
            }
            hashtag.getPosts().add(post);
            post.getHashtags().add(hashtag);
        }
    }

    public void removeFromGraph(Post post) {

        // Remove reference of users who gave like...
        for (String usernameWhoLiked: post.getLikedByUsernames()) {
            User likedBy = (User) this.usersByUsername.get(usernameWhoLiked);
            likedBy.getLikedPosts().removeByIntegerKey(post.getKey());
        }

        // Remove reference from author...
        User author = (User) usersByUsername.get(post.getPublishedByUsername());
        if (author != null) {
            author.getPosts().removeByIntegerKey(post.getKey());
        }

        // Remove post from hashtag list...
        for (String hashtagId: post.getHashtagIds()) {
            Hashtag hashtag = (Hashtag) hashtagsByName.get(hashtagId);
            hashtag.getPosts().removeByIntegerKey(post.getKey());
        }
    }

}
