package controller;

import datastructures.AVLTree.AVLTree;
import datastructures.LinkedList.LinkedList;
import models.Hashtag;
import models.Post;
import models.User;
import utils.JsonReader;
import utils.print.TreePrinter;

import java.io.FileNotFoundException;
import java.util.*;

public class InstaSalle {
    public static Scanner scanner;

    // Graph
    private LinkedList usersByUsername;
    private LinkedList postsById;
    private LinkedList hashtagsByName;

    // AVL Tree
    private AVLTree avlTree;

    public InstaSalle() {
        scanner = new Scanner(System.in);
        avlTree = new AVLTree();
    }

    private void computeInitialGraph(User[] users, Post[] posts) {

        // Store users
        this.usersByUsername = new LinkedList();
        for (User user: users) {
            usersByUsername.insert(user);
        }

        // Store users
        this.postsById = new LinkedList();
        for (Post post: posts) {
            usersByUsername.insert(post);
        }

        // Compute graph for each user
        for (User user: users) {
            this.computeGraph(user);
        }

        this.hashtagsByName = new LinkedList();

        // Compute graph for each post
        for (Post post: posts) {
            this.computeGraph(post);
        }
    }

    private void computeGraph(User user) {
        // Connect following users
        for (String usernameToFollow: user.getToFollowUsernames()) {
            User toFollow = (User) usersByUsername.getByKey(usernameToFollow.hashCode());
            user.getFollowing().add(toFollow);
            toFollow.getFollowers().add(user);
        }
    }

    private void computeGraph(Post post) {

        // Post liked by...
        for (String usernameWhoLiked: post.getLikedByUsernames()) {
            User likedBy = (User) usersByUsername.getByKey(usernameWhoLiked.hashCode());
            post.getLikedBy().add(likedBy);
            likedBy.getLikedPosts().add(post);
        }

        // Post published by...
        User author = (User) usersByUsername.getByKey(post.getPublishedByUsername().hashCode());
        author.getPosts().add(post);
        post.setPublishedBy(author);

        // Post with hashtags...
        for (String hashtagId: post.getHashtagIds()) {
            Hashtag hashtag = (Hashtag) hashtagsByName.getByKey(hashtagId.hashCode());
            if (hashtag == null) {
                hashtag = new Hashtag(hashtagId);
                hashtagsByName.insert(hashtag);
            }
            hashtag.getPosts().add(post);
            post.getHashtags().add(hashtag);
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
                String fileNameUsers = scanner.nextLine();
                if (fileNameUsers.equals("")) {
                    fileNameUsers = "/users.json";
                }

                System.out.println("Specify path of the file to be imported corresponding to posts (default is /posts.json)");
                String fileNamePosts = scanner.nextLine();
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
                this.computeInitialGraph(users, posts);

                // Add data to AVL Tree
                for (Post post: posts) {
                    this.avlTree.insert(post);
                }

                timer.triggerEnd();
                System.out.println("Successful import!");
                System.out.println( (users.length + posts.length) + " elements added in " + timer.computeFormattedDuration());

                break;

            case 2:// Export files

                int exportOption;
                showExportMenu();

                try {
                    exportOption = Integer.parseInt(scanner.nextLine());
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
                    visualizationOption = Integer.parseInt(scanner.nextLine());
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
                    insertionOption = Integer.parseInt(scanner.nextLine());
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
                    eliminationOption = Integer.parseInt(scanner.nextLine());
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
                    searchOption = Integer.parseInt(scanner.nextLine());
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

                User user = new User();
                user.fillFromUserInput();

                // Graph
                this.computeGraph(user);
                this.usersByUsername.insert(user);

                // TODO Insert to Trie

                break;

            case 2:// New post

                Post post = new Post();
                post.fillFromUserInput();

                // Graph
                this.computeGraph(post);
                this.postsById.insert(post);

                // AVL Tree
                this.avlTree.insert(post);

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
                int idToFind = Integer.parseInt(scanner.nextLine());
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
    public void init() {
        int functionalityOption;

        while (true) {
            showFunctionalitiesMenu();
            try {
                functionalityOption = Integer.parseInt(scanner.nextLine());
                if (functionalityOption == 8) break;
                handleOption(functionalityOption);
            } catch (NumberFormatException e) {
                System.out.println("Wrong option, please input a number...");
            }
        }
    }
}
