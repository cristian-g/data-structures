package controller;

import datastructures.AVLTree.AVLTree;
import models.Hashtag;
import models.Post;
import models.User;
import utils.JsonReader;

import java.io.FileNotFoundException;
import java.util.*;

public class InstaSalle {
    private Scanner kb;

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
        System.out.println("    1. Import files");
        System.out.println("    2. Export files");
        System.out.println("    3. Visualization of the structure's state");
        System.out.println("    4. Insertion of information");
        System.out.println("    5. Erase information");
        System.out.println("    6. Search information");
        System.out.println("    7. Limit memory for autocomplete");
        System.out.println("    8. Exit");
        System.out.println("Select an option from the menu:");
    }

    public void showExportMenu() {
        System.out.println("Select an option:");
        System.out.println("    1. Export of files in JSON format of users and posts");
        System.out.println("    2. Export of files in JSON format of all the structures");
    }

    public void showVisualizationMenu() {
        System.out.println("What structure do you want to see?");
        System.out.println("    1. Trie");
        System.out.println("    2. R-Tree");
        System.out.println("    3. AVL Tree");
        System.out.println("    4. Hash table");
        System.out.println("    5. Graph");
    }

    public void showInsertionMenu() {
        System.out.println("What type of information do you want to insert?");
        System.out.println("    1. New user");
        System.out.println("    2. New post");
    }

    public void showEliminationMenu() {
        System.out.println("What type of information do you want to delete?");
        System.out.println("    1. User");
        System.out.println("    2. Post");
    }

    public void showSearchMenu() {
        System.out.println("What type of information do you want to search?");
        System.out.println("    1. User");
        System.out.println("    2. Post");
        System.out.println("    3. According to hashtag");
        System.out.println("    4. According to location");
        System.out.println("    5. Personalized");
    }

    /**
     * Start one of the application options.
     *
     * @param functionalityOption The selected functionality.
     */
    private void handleOption(int functionalityOption) {

        switch (functionalityOption) {

            case 1:// Import files

                System.out.println("Specify path of the file to import corresponding to users (default is /users.json):");
                String fileNameUsers = kb.nextLine();
                if (fileNameUsers.equals("")) {
                    fileNameUsers = "/users.json";
                }

                System.out.println("Specify path of the file to be imported corresponding to posts (default is /posts.json)");
                String fileNamePosts = kb.nextLine();
                if (fileNamePosts.equals("")) {
                    fileNamePosts = "/posts.json";
                }

                System.out.println("Loading info...\n");

                // Start timer
                Timer timer = new Timer();
                timer.triggerStart();

                // Import data to arrays (they will be discarded by java garbage collector
                // after adding all the elements into the data structures)

                User[] users = null;
                try {
                    users = JsonReader.parseUsers(fileNameUsers);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Post[] posts = null;
                try {
                    posts = JsonReader.parsePosts(fileNamePosts);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // Compute graph
                this.computeGraph(users, posts);

                // Add data to AVL Tree
                for (Post post: posts) {
                    avlTree.setRoot(this.avlTree.insert(this.avlTree.getRoot(), post));
                }

                timer.triggerEnd();
                System.out.println("Successful import!");
                System.out.println( (users.length + posts.length) + " elements added in " + timer.computeFormattedDuration());

                break;

            case 2:// Export files

                int exportOption;
                showExportMenu();

                try {
                    exportOption = Integer.parseInt(kb.nextLine());
                    this.handleExportOption(exportOption);
                }
                catch (NumberFormatException e) {
                    System.out.println("Wrong option.\n");
                }

                break;

            case 3:// Visualization of the structure's state

                int visualizationOption;
                showVisualizationMenu();

                try {
                    visualizationOption = Integer.parseInt(kb.nextLine());
                    this.handleVisualizationOption(visualizationOption);
                }
                catch (NumberFormatException e) {
                    System.out.println("Wrong option.\n");
                }

                break;

            case 4:// Insertion of information

                int insertionOption;
                showInsertionMenu();

                try {
                    insertionOption = Integer.parseInt(kb.nextLine());
                    this.handleInsertionOption(insertionOption);
                }
                catch (NumberFormatException e) {
                    System.out.println("Wrong option.\n");
                }

                break;

            case 5:// Erase information

                int eliminationOption;
                showEliminationMenu();

                try {
                    eliminationOption = Integer.parseInt(kb.nextLine());
                    this.handleEliminationOption(eliminationOption);
                }
                catch (NumberFormatException e) {
                    System.out.println("Wrong option.\n");
                }

                break;

            case 6:// Search information

                int searchOption;
                showSearchMenu();

                try {
                    searchOption = Integer.parseInt(kb.nextLine());
                    this.handleSearchOption(searchOption);
                }
                catch (NumberFormatException e) {
                    System.out.println("Wrong option.\n");
                }

                break;

            case 7:// Limit memory for autocomplete

                // TODO limit memory for autocomplete
                System.out.println("TODO limit memory for autocomplete");

                break;

            case 8://Exit
                break;

            default://Wrong option
                System.out.println("Choose an option from 1 to 8");
                break;
        }
    }

    /**
     * Start one of the application options.
     *
     * @param visualizationOption The selected functionality.
     */
    private void handleVisualizationOption(int visualizationOption) {

        switch (visualizationOption) {

            case 1:// Trie visualization

                // TODO Trie visualization
                System.out.println("TODO Trie visualization");

                break;

            case 2:// R-Tree visualization

                // TODO R-Tree visualization
                System.out.println("TODO R-Tree visualization");

                break;

            case 3:// AVL Tree visualization

                System.out.println("Inorder:");
                avlTree.inOrder(avlTree.getRoot());

                break;

            case 4:// Hash table visualization

                // TODO Hash table visualization
                System.out.println("TODO Hash table visualization");

                break;

            case 5:// Graph visualization

                // TODO pending of email answer
                /*for (User user: this.users) {
                    System.out.println(user);
                }

                for (Post post: this.posts) {
                    System.out.println(post);
                }*/

                break;
        }
    }

    /**
     * Start one of the application options.
     *
     * @param exportOption The selected functionality.
     */
    private void handleExportOption(int exportOption) {

        switch (exportOption) {

            case 1:// Export of files in JSON format of users and posts

                // TODO export of files in JSON format of users and posts
                System.out.println("TODO export of files in JSON format of users and posts");

                break;

            case 2:// Export of files in JSON format of all structures

                // TODO export of files in JSON format of all structures
                System.out.println("TODO export of files in JSON format of all structures");

                break;
        }
    }

    /**
     * Start one of the application options.
     *
     * @param insertionOption The selected functionality.
     */
    private void handleInsertionOption(int insertionOption) {

        switch (insertionOption) {

            case 1:// New user

                // TODO insertion of new user
                System.out.println("TODO insertion of new user");

                break;

            case 2:// New post

                // TODO insertion of new post
                System.out.println("TODO insertion of new post");

                break;
        }
    }

    /**
     * Start one of the application options.
     *
     * @param eliminationOption The selected functionality.
     */
    private void handleEliminationOption(int eliminationOption) {

        switch (eliminationOption) {

            case 1:// Delete user

                // TODO elimination of user
                System.out.println("TODO elimination of user");

                break;

            case 2:// Delete post

                // TODO elimination of post
                System.out.println("TODO elimination of post");

                break;
        }
    }

    /**
     * Start one of the application options.
     *
     * @param searchOption The selected functionality.
     */
    private void handleSearchOption(int searchOption) {

        switch (searchOption) {

            case 1:// Search user

                // TODO search user
                System.out.println("TODO search user");

                break;

            case 2:// Search post

                System.out.println("Enter post id:");
                int idToFind = Integer.parseInt(kb.nextLine());
                System.out.println(avlTree.findNodeWithKey(idToFind));

                break;

            case 3:// Search according to hashtag

                // TODO search according to hashtag
                System.out.println("TODO search according to hashtag");

                break;

            case 4:// Search according to location

                // TODO search according to location
                System.out.println("TODO search according to location");

                break;

            case 5:// Search personalized

                // TODO search personalized
                System.out.println("TODO search personalized");

                break;
        }
    }

    /**
     * This method allows toId enter options toId execute.
     */
    public boolean init() {

        this.kb = new Scanner(System.in);

        int functionalityOption;
        showFunctionalitiesMenu();
        boolean close = false;

        try {
            functionalityOption = kb.nextInt();
            kb.nextLine();
            this.handleOption(functionalityOption);
            close = functionalityOption == 8;
        }
        catch (NumberFormatException e) {
            System.out.println("Wrong option.\n");
        }

        kb.close();
        return close;
    }
}
