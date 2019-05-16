package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datastructures.AVLTree.AVLTree;
import datastructures.HashTable.HashTable;
import datastructures.LinkedList.LinkedList;
import datastructures.RTree.RTree;
import datastructures.Trie.Trie;
import models.Hashtag;
import models.Post;
import models.User;
import utils.JsonReader;
import utils.print.TreePrinter;

import java.io.*;
import java.util.*;

public class InstaSalle {
    public static Scanner scanner;

    // Lists
    private LinkedList<User> usersList;
    private LinkedList<Post> postsList;
    private LinkedList<Hashtag> hashtagsList;

    // Hash tables
    private HashTable<User> usersByUsername;
    private HashTable<Hashtag> hashtagsByName;

    // Trie
    private Trie trie;

    // AVL Tree
    private AVLTree avlTree;

    // R-Tree
    private RTree rTree;

    public InstaSalle() {
        scanner = new Scanner(System.in);

        // Init structures
        this.usersList = new LinkedList<>();
        this.postsList = new LinkedList<>();
        this.hashtagsList = new LinkedList<>();
        this.trie = new Trie();
        this.avlTree = new AVLTree();
    }

    private void computeInitialGraph(User[] users, Post[] posts) {

        // Compute graph for each user
        for (User user: users) {
            this.computeGraph(user);
        }

        // Compute graph for each post
        for (Post post: posts) {
            this.computeGraph(post);
        }
    }

    private void computeGraph(User user) {
        // Connect following users
        for (String usernameToFollow: user.getToFollowUsernames()) {
            User toFollow = (User) usersByUsername.get(usernameToFollow);
            user.getFollowing().add(toFollow);
            toFollow.getFollowers().add(user);
        }
    }

    private void removeFromGraph(User user) {
        // Disconnect follower users
        LinkedList<User> linkedList = user.getFollowers();
        User[] array = linkedList.toArray(new User[linkedList.getSize()]);
        for (User follower: array) {
            follower.getFollowing().removeByStringKey(user.getKey());
        }
    }

    private void computeGraph(Post post) {

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

    private void removeFromGraph(Post post) {

        // Remove reference of users who gave like...
        for (String usernameWhoLiked: post.getLikedByUsernames()) {
            User likedBy = (User) this.usersByUsername.get(usernameWhoLiked);
            likedBy.getLikedPosts().removeByIntegerKey(post.getKey());
        }

        // Remove reference from author...
        User author = (User) usersByUsername.get(post.getPublishedByUsername());
        author.getPosts().removeByIntegerKey(post.getKey());

        // Remove post from hashtag list...
        for (String hashtagId: post.getHashtagIds()) {
            Hashtag hashtag = (Hashtag) hashtagsByName.get(hashtagId);
            hashtag.getPosts().removeByIntegerKey(post.getKey());
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
        System.out.println("    3. Export of files in JSON format of an specific structure");
    }

    public void showExportSpecificMenu() {
        System.out.println("Which structure do you want to export?");
        System.out.println("    1. Trie");
        System.out.println("    2. R-Tree");
        System.out.println("    3. AVL Tree");
        System.out.println("    4. Hash table");
        System.out.println("    5. Graph");
    }

    public void showVisualizationMenu() {
        System.out.println("Which structure do you want to see?");
        System.out.println("    1. Trie");
        System.out.println("    2. R-Tree");
        System.out.println("    3. AVL Tree");
        System.out.println("    4. Hash table");
        System.out.println("    5. Graph");
    }

    public void showInsertionMenu() {
        System.out.println("Which type of information do you want to add?");
        System.out.println("    1. New user");
        System.out.println("    2. New post");
    }

    public void showEliminationMenu() {
        System.out.println("Which type of information do you want to delete?");
        System.out.println("    1. User");
        System.out.println("    2. Post");
    }

    public void showSearchMenu() {
        System.out.println("Which type of information do you want to search?");
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

                // Store users into list and hash table
                for (User user: users) {
                    usersList.add(user);
                    usersByUsername.insert(user);
                }

                // Store posts into list and hash table
                for (Post post: posts) {
                    postsList.add(post);
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

        TreePrinter treePrinter = new TreePrinter();

        switch (visualizationOption) {

            case 1:// Trie visualization

                System.out.println("Rendering image of Trie...");
                try {
                    System.out.println("Rendered image successfully: " + treePrinter.printTrie(this.trie));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case 2:// R-Tree visualization

                System.out.println("Rendering image of R-Tree...");
                try {
                    System.out.println("Rendered image successfully: " + treePrinter.printRTree(this.rTree));
                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;

            case 3:// AVL Tree visualization

                System.out.println("Inorder:");
                avlTree.inOrder(avlTree.getRoot());

                System.out.println("Rendering image of AVL Tree...");
                try {
                    System.out.println("Rendered image successfully: " + treePrinter.printAVLTree(this.avlTree));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case 4:// Hash table visualization

                System.out.println("TODO Hash table visualization");

                System.out.println("Rendering image of hash table...");
                try {
                    System.out.println("Rendered image successfully: " + treePrinter.printHashTable(this.hashtagsByName));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case 5:// Graph visualization

                LinkedList<User> linkedList1 = this.usersList;
                User[] array1 = linkedList1.toArray(new User[linkedList1.getSize()]);
                for (User user: array1) {
                    System.out.println(user);
                }

                LinkedList<Post> linkedList2 = this.postsList;
                Post[] array2 = linkedList2.toArray(new Post[linkedList2.getSize()]);
                for (Post post: array2) {
                    System.out.println(post);
                }

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

                // Export users
                try (Writer writer = new FileWriter("out/datasets/users.json")) {
                    Gson gson = new GsonBuilder().create();
                    User[] usersArray = this.usersList.toArray(new User[this.usersList.getSize()]);
                    gson.toJson(usersArray, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Export posts
                try (Writer writer = new FileWriter("out/datasets/posts.json")) {
                    Gson gson = new GsonBuilder().create();
                    Post[] postsArray = this.postsList.toArray(new Post[this.postsList.getSize()]);
                    gson.toJson(postsArray, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case 2:// Export of files in JSON format of all structures

                // TODO export of files in JSON format of all structures
                System.out.println("TODO export of files in JSON format of all structures");

                break;

            case 3:// Export of files in JSON format of an specific structure

                int exportSpecificOption;
                showExportSpecificMenu();

                try {
                    exportSpecificOption = Integer.parseInt(scanner.nextLine());
                    this.handleVisualizationOption(exportSpecificOption);
                }
                catch (NumberFormatException e) {
                    System.out.println("Wrong option.\n");
                }

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

                // Add to list
                this.usersList.add(user);

                // Add to hashtable
                this.usersByUsername.insert(user);

                // Insert to Trie
                this.trie.addUser(user.getUsername());

                break;

            case 2:// New post

                Post post = new Post();
                post.fillFromUserInput();

                // Graph
                this.computeGraph(post);

                // Add to list
                this.postsList.add(post);

                // Add to AVL Tree
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

                System.out.println("Specify username of the user you want do delete:");
                String desiredUsername = scanner.nextLine();

                System.out.println("Processing request...");

                // Find user
                User user = this.usersByUsername.get(desiredUsername);

                // Remove from graph
                // Disconnect follower users
                this.removeFromGraph(user);

                // Remove from list
                this.usersList.removeByStringKey(user.getKey());

                // Remove from hashtable
                this.usersByUsername.remove(user.getKey());

                // Remove from Trie
                this.trie.deleteUser(user.getUsername());

                System.out.println("\nThe user \"" + desiredUsername + "\" has been successfully removed from the system.\n");

                break;

            case 2:// Delete post

                System.out.println("Specify ID of the post you want do delete:");
                int desiredPost = Integer.parseInt(scanner.nextLine());

                System.out.println("Processing request...");

                // Find post
                Post post = (Post) this.avlTree.findNodeWithKey(desiredPost);

                // Remove from graph
                this.removeFromGraph(post);

                // Remove from list
                this.postsList.removeByIntegerKey(desiredPost);

                System.out.println("\nThe post with ID " + desiredPost + " has been successfully removed from the system.\n");

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

                System.out.println("Specify username of the user you want to search:");
                String desiredUsername = scanner.nextLine();

                System.out.println("Processing request...");

                break;

            case 2:// Search post

                System.out.println("Enter post id:");
                int idToFind = Integer.parseInt(scanner.nextLine());
                System.out.println(avlTree.findNodeWithKey(idToFind));

                break;

            case 3:// Search according to hashtag

                System.out.println("Enter hashtag:");
                String desiredHashtag = scanner.nextLine();

                Hashtag hashtag = this.hashtagsByName.get(desiredHashtag);

                int desiredSize = 5;
                Post[] posts = hashtag.getPosts().toArrayOfFirst(new Post[desiredSize], desiredSize);
                for (Post post: posts) {
                    System.out.println(post);
                }

                break;

            case 4:// Search according to location

                // TODO search according to location
                System.out.println("TODO search according to location");

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

                try {
                    handleOption(functionalityOption);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (NumberFormatException e) {
                System.out.println("Wrong option, please input a number...");
            }
        }
    }
}
