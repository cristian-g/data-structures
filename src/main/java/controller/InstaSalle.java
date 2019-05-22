package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import datastructures.AVLTree.AVLTree;
import datastructures.Graph.Graph;
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
    private HashTable<Post> postsById;
    private HashTable<Hashtag> hashtagsByName;

    // Graph
    private Graph graph;

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
        this.rTree = new RTree();
        this.usersByUsername = new HashTable<>();
        this.postsById = new HashTable<>();
        this.hashtagsByName = new HashTable<>();
        this.graph = new Graph(usersByUsername, hashtagsByName);
    }

    public void showFunctionalitiesMenu() {
        System.out.println("\n---------- InstaSalle ------------");
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
        System.out.println("    2. Export of images of all the structures");
        System.out.println("    3. Export of image of an specific structure");
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
                System.out.println("Hint: /extra/[small/medium/large]/users.json");
                String fileNameUsers = scanner.nextLine();
                if (fileNameUsers.equals("")) {
                    fileNameUsers = "/users.json";
                }

                System.out.println("Specify path of the file to be imported corresponding to posts (default is /posts.json)");
                System.out.println("Hint: /extra/[small/medium/large]/posts.json");
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

                // Store users into list, hash table and trie
                for (User user: users) {
                    usersList.add(user);
                    usersByUsername.insert(user);
                    trie.addUser(user);
                }

                // Store posts into list and hash table
                for (Post post: posts) {
                    String[] hashtagIds = post.getHashtagIds();
                    for (int i = 0; i < hashtagIds.length; i++) {
                        String hashtag = hashtagIds[i];
                        if (hashtag.charAt(0) == '#') {
                            hashtag = hashtag.substring(1);
                        }
                        hashtagIds[i] = hashtag;
                    }
                    postsList.add(post);
                    postsById.insert(post);
                }

                // Compute Graph
                this.graph.computeInitialGraph(users, posts);

                // Add data to AVL Tree and R-Tree
                for (Post post: posts) {
                    this.avlTree.insert(post);
                    this.rTree.addPost(post);
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

                System.out.println("At the moment the limit is set at " + trie.getLimit() + " words");
                System.out.println("What do you want as new limit?");

                int desiredLimit = Integer.parseInt(scanner.nextLine());

                System.out.println("Processing request...");

                trie.limitMemory(desiredLimit);

                break;

            case 8://Exit
                break;

            default://Wrong option
                System.out.println("Choose an option from 1 to 8");
                break;
        }
    }

    private void exportTrie(TreePrinter treePrinter) {
        try {
            System.out.println("Rendering image of Trie...");
            System.out.println("Rendered image successfully: " + treePrinter.printTrie(this.trie));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportRTree(TreePrinter treePrinter) {
        try {
            System.out.println("Rendering image of R-Tree...");
            System.out.println("Rendered image successfully: " + treePrinter.printRTree(this.rTree));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportAVLTree(TreePrinter treePrinter) {
        try {
            System.out.println("Inorder:");
            avlTree.inOrder(avlTree.getRoot());
            System.out.println("Rendering image of AVL Tree...");
            System.out.println("Rendered image successfully: " + treePrinter.printAVLTree(this.avlTree));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportHashTable(TreePrinter treePrinter) {
        try {
            System.out.println("Rendering image of hash table...");
            System.out.println("Rendered image successfully: " + treePrinter.printHashTable(this.hashtagsByName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportGraph() {
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

                this.exportTrie(treePrinter);

                break;

            case 2:// R-Tree visualization

                this.exportRTree(treePrinter);

                break;

            case 3:// AVL Tree visualization

                this.exportAVLTree(treePrinter);

                break;

            case 4:// Hash table visualization

                this.exportHashTable(treePrinter);

                break;

            case 5:// Graph visualization

                this.exportGraph();

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

            case 2:// Export of images of all structures

                System.out.println("Exporting images of all structures...");

                this.exportTrie(new TreePrinter());// Trie
                this.exportRTree(new TreePrinter());// R-Tree
                this.exportAVLTree(new TreePrinter());// AVL Tree
                this.exportHashTable(new TreePrinter());// Hash table
                this.exportGraph();// Graph visualization

                break;

            case 3:// Export of image of an specific structure

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
                user.fillFromUserInput(usersByUsername, postsById);

                // Graph
                this.graph.computeGraph(user);

                // Add to list
                this.usersList.add(user);

                // Add to hashtable
                this.usersByUsername.insert(user);

                // Insert to Trie
                this.trie.addUser(user);

                break;

            case 2:// New post

                Post post = new Post();
                post.fillFromUserInput(usersByUsername, postsById);

                // Graph
                this.graph.computeGraph(post);

                // Add to list
                this.postsList.add(post);

                // Add to hashtable
                this.postsById.insert(post);

                // Add to AVL Tree
                this.avlTree.insert(post);

                // Add to R-Tree
                this.rTree.addPost(post);

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

                String desiredUsername = null;

                while (true) {
                    System.out.println("Specify username of the user you want do delete:");
                    desiredUsername = scanner.nextLine();
                    User user = (User) usersByUsername.get(desiredUsername);
                    if (user == null) {
                        System.out.println("Username " + desiredUsername + " has not been found... Please, try again.");
                    }
                    else {
                        break;
                    }
                }

                System.out.println("Processing request...");

                // Find user
                User user = this.usersByUsername.get(desiredUsername);

                // Remove from Graph
                // Disconnect follower users
                this.graph.removeFromGraph(user);

                // Remove from list
                this.usersList.removeByStringKey(user.getKey());

                // Remove from hashtable
                this.usersByUsername.remove(user.getKey());

                // Remove from Trie
                this.trie.deleteUser(user.getUsername());

                System.out.println("\nThe user \"" + desiredUsername + "\" has been successfully removed from the system.\n");

                break;

            case 2:// Delete post

                int desiredPost = -1;

                while (true) {
                    System.out.println("Specify ID of the post you want do delete:");
                    desiredPost = Integer.parseInt(scanner.nextLine());
                    Post post = (Post) postsById.get(desiredPost);
                    if (post == null) {
                        System.out.println("Post with ID " + desiredPost + " has not been found... Please, try again.");
                    }
                    else {
                        break;
                    }
                }

                System.out.println("Processing request...");

                // Find post
                Post post = (Post) this.avlTree.findNodeWithKey(desiredPost);

                // Remove from AVL
                this.avlTree.deleteNode(post.getKey());

                // Remove from R-Tree
                this.rTree.removePost(post);

                // Remove from Graph
                this.graph.removeFromGraph(post);

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

                java.lang.String desiredUsername = "";
                User desiredUser = null;
                User[] suggestionsArray = null;
                int desiredOption = -1;

                while (true) {
                    String toAdd = scanner.nextLine();

                    desiredUsername = desiredUsername + toAdd;

                    final LinkedList<User> suggestions = trie.getSuggestions(desiredUsername);
                    suggestionsArray = suggestions.toArray(new User[suggestions.getSize()]);
                    System.out.println("Suggestions:");
                    for (int i = 0; i < suggestionsArray.length; i++) {
                        System.out.println((i+1) + ". " + suggestionsArray[i].getUsername());
                    }
                    System.out.println((suggestionsArray.length+1) + ". None of the suggested");

                    desiredOption = Integer.parseInt(scanner.nextLine());
                    if (desiredOption != suggestionsArray.length+1) {
                        desiredUser = suggestionsArray[desiredOption - 1];
                        desiredUsername =  desiredUser.getUsername();
                    }
                    System.out.println("Load information of user [" + desiredUsername + "]? [Y/N]");
                    String answer = scanner.nextLine();
                    if (answer.toLowerCase().equals("y")) {
                        break;
                    }
                }

                if (desiredUser == null) {
                    System.out.println("We are sorry but we have not found any user with the username that you have entered (" + desiredUsername + ").");
                }
                else {
                    System.out.println("\n" + desiredUser.toString());
                }

                break;

            case 2:// Search post

                System.out.println("Enter post id:");
                int idToFind = Integer.parseInt(scanner.nextLine());
                System.out.println(avlTree.findNodeWithKey(idToFind));

                break;

            case 3:// Search according to hashtag

                System.out.println("Enter hashtag (without #):");
                String desiredHashtag = scanner.nextLine();

                Hashtag hashtag = this.hashtagsByName.get(desiredHashtag);

                if (hashtag == null) {
                    System.out.println("We are sorry but we have not found any post with the hashtag that you have entered (" + desiredHashtag + ").");
                }
                else {
                    int desiredSize = 5;
                    Post[] posts = hashtag.getPosts().toArrayOfFirst(new Post[desiredSize], desiredSize);
                    System.out.println("Showing first " + posts.length + " of a total of " +  hashtag.getPosts().getSize() + " with hashtag #" + desiredHashtag + "...");
                    for (Post post: posts) {
                        System.out.println(post);
                    }
                }

                break;

            case 4: // Search according to location
                System.out.println("Latitud 1:");
                double lat1 = Double.parseDouble(scanner.nextLine());
                System.out.println("Longitud 1:");
                double lon1 = Double.parseDouble(scanner.nextLine());
                System.out.println("Latitud 2:");
                double lat2 = Double.parseDouble(scanner.nextLine());
                System.out.println("Longitud 2:");
                double lon2 = Double.parseDouble(scanner.nextLine());

                LinkedList<Post> postsLinkedList = rTree.getPosts(new double[]{lat1, lon1}, new double[]{lat2, lon2});
                System.out.println(postsLinkedList.getSize());
                Post[] posts = postsLinkedList.toArray(new Post[postsLinkedList.getSize()]);

                System.out.println(rTree.getPost(new double[]{112.42034291299234, 44.72663866389317}));

                System.out.println("S'han trobat " + posts.length + " posts");
                for (Post p : posts) {
                    System.out.println(p);
                }

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
