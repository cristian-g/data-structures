package controller;

import com.google.gson.JsonIOException;
import datastructures.AVLTree.AVLTree;
import models.Hashtag;
import models.Post;
import models.User;
import utils.JsonReader;

import java.io.FileNotFoundException;
import java.util.*;

public class InstaSalle {
    private Scanner kb;

    // Graph


    // AVL Tree
    private AVLTree avlTree = new AVLTree();

    public InstaSalle() {
    }

    private void computeGraph(User[] users, Post[] posts) {

        HashMap<String, User> usersByUsername = new HashMap<>();
        for (User user: users) {
            usersByUsername.put(user.getUsername(), user);
        }
        for (User user: users) {
            for (String usernameToFollow: user.getToFollowUsernames()) {
                User toFollow = usersByUsername.get(usernameToFollow);
                user.getFollowing().add(toFollow);
                toFollow.getFollowers().add(user);
            }
        }

        HashMap<String, Hashtag> hashtagsByName = new HashMap<>();
        for (Post post: posts) {
            // Post liked by...
            for (String usernameWhoLiked: post.getLikedByUsernames()) {
                User likedBy = usersByUsername.get(usernameWhoLiked);
                post.getLikedBy().add(likedBy);
                likedBy.getLikedPosts().add(post);
            }
            // Post published by...
            User author = usersByUsername.get(post.getPublishedByUsername());
            author.getPosts().add(post);
            post.setPublishedBy(author);

            // Post with hashtags...
            for (String hashtagId: post.getHashtagIds()) {
                Hashtag hashtag = hashtagsByName.get(hashtagId);
                if (hashtag == null) {
                    hashtag = new Hashtag(hashtagId);
                    hashtagsByName.put(hashtagId, hashtag);
                }
                hashtag.getPosts().add(post);
                post.getHashtags().add(hashtag);
            }
        }
    }

    public void showFunctionalitiesMenu() {
        System.out.println("---------- InstaSalle ------------");
        System.out.println("1. Import files");
        System.out.println("2. Export files");
        System.out.println("3. Visualization of the structure's state");
        System.out.println("4. Insertion of information");
        System.out.println("5. Erase information");
        System.out.println("6. Search information");
        System.out.println("Select an option from the menu:");
    }

    /**
     * Start one of the application options.
     *
     * @param functionalityOption The selected functionality.
     */
    private void handleOption(int functionalityOption) {

        switch (functionalityOption) {

            case 1:// Import files

                // Import data to arrays (they will be discarded by java garbage collector
                // after adding all the elements into the data structures)

                User[] users = null;
                try {
                    users = JsonReader.parseUsers();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Post[] posts = null;
                try {
                    posts = JsonReader.parsePosts();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // Graph
                this.computeGraph(users, posts);


                // AVL Tree
                for (Post post: posts) {
                    System.out.println("adding post: " + post);
                    System.out.println(post);
                    avlTree.setRoot(this.avlTree.insert(this.avlTree.getRoot(), post));
                }

                break;

            case 2:// Export files

                break;

            case 3:// Visualization of the structure's state



                // AVL Tree
                System.out.println("Preorder:");
                avlTree.preOrder(avlTree.getRoot());

                System.out.println("Inorder:");
                avlTree.inOrder(avlTree.getRoot());


                // Graph
                // TODO pending of email answer
                /*for (User user: this.users) {
                    System.out.println(user);
                }

                for (Post post: this.posts) {
                    System.out.println(post);
                }*/



                break;

            case 4:// Insertion of information

                break;

            case 5:// Erase information

                break;

            case 6:// Search information

                int idToFind = Integer.parseInt(kb.nextLine());

                System.out.println(avlTree.findNodeWithKey(idToFind));

                break;

            default:

                break;
        }
    }

    /**
     * This method allows toId enter options toId execute.
     */
    public void init() {
        int functionalityOption;
        boolean exit = false;
        this.kb = new Scanner(System.in);

        showFunctionalitiesMenu();

        try {
            functionalityOption = Integer.parseInt(kb.nextLine());
            this.handleOption(functionalityOption);
        } catch (NumberFormatException e) {
            System.out.println("Wrong option.\n");
        }

        kb.close();
    }
}
