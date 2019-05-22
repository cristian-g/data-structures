package timetest;

import controller.Timer;
import datastructures.AVLTree.AVLTree;
import datastructures.ElementWithIntegerKey;
import datastructures.ElementWithStringKey;
import datastructures.Graph.Graph;
import datastructures.HashTable.HashTable;
import datastructures.RTree.RTree;
import datastructures.Trie.Trie;
import models.Post;
import models.User;
import test.SimpleElementWithIntegerKey;
import test.SimpleElementWithStringKey;
import utils.IntegerUtilities;
import utils.JsonReader;
import utils.ObjectFactory;
import utils.print.CSVPrinter;
import utils.print.TreePrinter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class TimeTest {
    public final String filename1 = "insertion_n_elements";// Insertion of n elements in an empty structure
    public final String filename1Graph = "insertion_n_elements_graph";
    public final String filename2 = "insertion_1_element_in_n_elements";// Insertion of an element having n elements
    public final String filename2Graph = "insertion_1_element_in_n_elements_graph";
    public final String filename3 = "search_1_element_in_n_elements";// Search for an element having n elements
    public final String filename3Graph = "search_1_element_in_n_elements_graph";
    public final String filename4 = "elimination_1_element_in_n_elements";// Elimination of an element having n elements
    public final String filename4Graph = "elimination_1_element_in_n_elements_graph";
    public final String filename5 = "elimination_n_elements";// Elimination of n elements (until leaving empty structure)
    public final String filename5Graph = "elimination_n_elements_graph";

    public void runTimeTest1() {

        CSVPrinter csvPrinter = new CSVPrinter();

        int[] sizes = new int[10];
        int length = sizes.length;
        //for (int i = 0; i < length; i++) sizes[i] = (int) Math.pow(10, i+3);
        sizes[0] = 10000;
        sizes[1] = 50000;
        sizes[2] = 100000;
        sizes[3] = 150000;
        sizes[4] = 200000;
        sizes[5] = 250000;
        sizes[6] = 300000;
        sizes[7] = 400000;
        sizes[8] = 500000;
        sizes[9] = 600000;

        System.out.println("\n" + "--------------------");
        System.out.println("Starting the test... We will try collections of the following sizes:");
        System.out.println(Arrays.toString(sizes));
        System.out.println("--------------------" + "\n");

        Object[] dataStructures = new Object[] {
                new Trie(),// Trie
                new RTree(),// RTree
                new AVLTree(),// AVLTree
                new HashTable<SimpleElementWithStringKey>(),// HashTable
                //new Graph(),// Graph
                new datastructures.LinkedList.LinkedList(),// LinkedList
        };

        int count = 0;
        for (Object dataStructure: dataStructures) {
            // -------------------------------------
            this.registerDataStructure(dataStructure, csvPrinter);
            // -------------------------------------

            for (int size: sizes) {

                if (size > 225000 && dataStructure instanceof Trie) {
                    csvPrinter.getTimes().get(count).add(new Double(0));
                    continue;
                }

                System.out.println("Size: " + size);

                // Init data structure
                dataStructure = this.initDataStructure(dataStructure);

                // Generate elements using generated random keys
                Object[] elements = this.computeElements(dataStructure, size);

                // Insert elements into the data structure
                Timer timer = new Timer();
                timer.triggerStart();
                for (Object element: elements) {
                    this.insert(dataStructure, element);
                }
                timer.triggerEnd();

                csvPrinter.getTimes().get(count).add(timer.computeDuration());
            }
            count++;
        }

        csvPrinter.setnOfElements(sizes);

        csvPrinter.print(filename1);
    }

    public void runTimeTest1Graph() {

        CSVPrinter csvPrinter = new CSVPrinter();

        System.out.println("\n" + "--------------------");
        System.out.println("Starting the test...");
        System.out.println("--------------------" + "\n");

        Object[] dataStructures = new Object[] {
                new Graph(),// Graph
        };

        Object dataStructure = dataStructures[0];
        // -------------------------------------
        this.registerDataStructure(dataStructure, csvPrinter);
        // -------------------------------------

        // Init data structure
        dataStructure = this.initDataStructure(dataStructure);

        // Import data from json files

        final String[] filenamesUsers = new String[] {
                "/extra/small/users.json",
                "/extra/medium/users.json",
                "/extra/large/users.json",
        };

        final String[] filenamesPosts = new String[] {
                "/extra/small/posts.json",
                "/extra/medium/posts.json",
                "/extra/large/posts.json",
        };

        int[] sizes = new int[filenamesUsers.length];

        for (int i = 0; i < filenamesUsers.length; i++) {
            // Import data to arrays (they will be discarded by java garbage collector
            // after adding all the elements into the data structures)

            User[] users = null;
            try {
                users = JsonReader.parseUsers(filenamesUsers[i]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            sizes[i] = users.length;

            Post[] posts = null;
            try {
                posts = JsonReader.parsePosts(filenamesPosts[i]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Graph graph = (Graph) dataStructure;

            // Store users into hash table
            for (User user: users) {
                graph.getUsersByUsername().insert(user);
            }

            // Start timer
            Timer timer = new Timer();
            timer.triggerStart();

            // Compute Graph
            graph.computeInitialGraph(users, posts);

            timer.triggerEnd();

            csvPrinter.getTimes().get(0).add(timer.computeDuration());

            System.out.println("Dataset " + filenamesUsers[i] + " in " + timer.computeFormattedDuration() + " (" + users.length + " users)");
        }

        csvPrinter.setnOfElements(sizes);

        csvPrinter.print(filename1Graph);
    }

    public void runTimeTest2() {

        int maxSize = 100000;

        CSVPrinter csvPrinter = new CSVPrinter();

        int[] sizes = IntegerUtilities.generateCounterArray(maxSize);

        System.out.println("\n" + "--------------------");
        System.out.println("Starting the test...");
        //System.out.println(Arrays.toString(sizes));
        System.out.println("--------------------" + "\n");

        Object[] dataStructures = new Object[] {
                new Trie(),// Trie
                new RTree(),// RTree
                new AVLTree(),// AVLTree
                new HashTable<SimpleElementWithStringKey>(),// HashTable
                //new Graph(),// Graph
                new datastructures.LinkedList.LinkedList(),// LinkedList
        };

        int count = 0;
        for (Object dataStructure: dataStructures) {
            // -------------------------------------
            this.registerDataStructure(dataStructure, csvPrinter);
            // -------------------------------------

            // Init data structure
            dataStructure = this.initDataStructure(dataStructure);

            // Generate elements using generated random keys
            Object[] elements = this.computeElements(dataStructure, maxSize);

            for (int size: sizes) {

                // Insert element into the data structure
                Timer timer = new Timer();
                timer.triggerStart();
                this.insert(dataStructure, elements[size]);
                timer.triggerEnd();

                csvPrinter.getTimes().get(count).add(timer.computeDuration());
            }
            count++;

            //this.printDataStructure(dataStructure);
        }

        csvPrinter.setnOfElements(sizes);

        csvPrinter.print(filename2);
    }

    public void runTimeTest3() {

        int maxSize = 100000;

        CSVPrinter csvPrinter = new CSVPrinter();

        int[] sizes = IntegerUtilities.generateCounterArray(maxSize);

        System.out.println("\n" + "--------------------");
        System.out.println("Starting the test...");
        //System.out.println(Arrays.toString(sizes));
        System.out.println("--------------------" + "\n");

        Object[] dataStructures = new Object[] {
                new Trie(),// Trie
                new RTree(),// RTree
                new AVLTree(),// AVLTree
                new HashTable<SimpleElementWithStringKey>(),// HashTable
                //new Graph(),// Graph
                new datastructures.LinkedList.LinkedList(),// LinkedList
        };

        int count = 0;
        for (Object dataStructure: dataStructures) {
            // -------------------------------------
            this.registerDataStructure(dataStructure, csvPrinter);
            // -------------------------------------

            // Init data structure
            dataStructure = this.initDataStructure(dataStructure);

            // Generate elements using generated random keys
            Object[] elements = this.computeElements(dataStructure, maxSize);

            for (int size: sizes) {

                // Insert element into the data structure
                this.insert(dataStructure, elements[size]);

                // Search element through the data structure
                Timer timer = new Timer();
                timer.triggerStart();
                this.search(dataStructure, elements[size]);
                timer.triggerEnd();

                csvPrinter.getTimes().get(count).add(timer.computeDuration());
            }
            count++;

            //this.printDataStructure(dataStructure);
        }

        csvPrinter.setnOfElements(sizes);

        csvPrinter.print(filename3);
    }

    public void runTimeTest4() {

        int maxSize = 100000;

        CSVPrinter csvPrinter = new CSVPrinter();

        int[] sizes = IntegerUtilities.generateCounterArray(maxSize);

        System.out.println("\n" + "--------------------");
        System.out.println("Starting the test...");
        //System.out.println(Arrays.toString(sizes));
        System.out.println("--------------------" + "\n");

        Object[] dataStructures = new Object[] {
                new Trie(),// Trie
                new RTree(),// RTree
                new AVLTree(),// AVLTree
                new HashTable<SimpleElementWithStringKey>(),// HashTable
                //new Graph(),// Graph
                new datastructures.LinkedList.LinkedList(),// LinkedList
        };

        int count = 0;
        for (Object dataStructure: dataStructures) {
            // -------------------------------------
            this.registerDataStructure(dataStructure, csvPrinter);
            // -------------------------------------

            // Init data structure
            dataStructure = this.initDataStructure(dataStructure);

            // Generate elements using generated random keys
            Object[] elements = this.computeElements(dataStructure, maxSize);

            for (int size: sizes) {
                // Insert elements into the data structure
                this.insert(dataStructure, elements[size]);
            }

            for (int size: sizes) {

                // Remove elements from the data structure
                Timer timer = new Timer();
                timer.triggerStart();
                Object element = elements[sizes[sizes.length - 1] - size];
                this.delete(dataStructure, element);
                timer.triggerEnd();

                csvPrinter.getTimes().get(count).add(timer.computeDuration());
            }
            count++;

            //this.printDataStructure(dataStructure);
        }

        csvPrinter.setnOfElements(sizes);

        csvPrinter.print(filename4);
    }

    public void runTimeTest5() {

        CSVPrinter csvPrinter = new CSVPrinter();

        int[] sizes = new int[10];
        int length = sizes.length;
        //for (int i = 0; i < length; i++) sizes[i] = (int) Math.pow(10, i+3);
        sizes[0] = 10000;
        sizes[1] = 50000;
        sizes[2] = 100000;
        sizes[3] = 150000;
        sizes[4] = 200000;
        sizes[5] = 250000;
        sizes[6] = 300000;
        sizes[7] = 400000;
        sizes[8] = 500000;
        sizes[9] = 600000;

        System.out.println("\n" + "--------------------");
        System.out.println("Starting the test... We will try collections of the following sizes:");
        System.out.println(Arrays.toString(sizes));
        System.out.println("--------------------" + "\n");

        Object[] dataStructures = new Object[] {
                new Trie(),// Trie
                new RTree(),// RTree
                new AVLTree(),// AVLTree
                new HashTable<SimpleElementWithStringKey>(),// HashTable
                //new Graph(),// Graph
                new datastructures.LinkedList.LinkedList(),// LinkedList
        };

        int count = 0;
        for (Object dataStructure: dataStructures) {
            // -------------------------------------
            this.registerDataStructure(dataStructure, csvPrinter);
            // -------------------------------------

            for (int size: sizes) {

                if (size > 225000 && dataStructure instanceof Trie) {
                    csvPrinter.getTimes().get(count).add(new Double(0));
                    continue;
                }

                System.out.println("Size: " + size);

                // Init data structure
                dataStructure = this.initDataStructure(dataStructure);

                // Generate elements using generated random keys
                Object[] elements = this.computeElements(dataStructure, size);

                // Insert elements into the data structure
                for (Object element: elements) {
                    this.insert(dataStructure, element);
                }

                // Delete elements from the data structure
                Timer timer = new Timer();
                timer.triggerStart();
                for (int i = 0; i < elements.length; i++) {
                    Object element = elements[elements.length-1-i];
                    this.delete(dataStructure, element);
                }
                timer.triggerEnd();

                csvPrinter.getTimes().get(count).add(timer.computeDuration());
            }
            count++;
        }

        csvPrinter.setnOfElements(sizes);

        csvPrinter.print(filename5);
    }

    public void runTimeTest5Graph() {

        CSVPrinter csvPrinter = new CSVPrinter();

        System.out.println("\n" + "--------------------");
        System.out.println("Starting the test...");
        System.out.println("--------------------" + "\n");

        Object[] dataStructures = new Object[] {
                new Graph(),// Graph
        };

        Object dataStructure = dataStructures[0];
        // -------------------------------------
        this.registerDataStructure(dataStructure, csvPrinter);
        // -------------------------------------

        // Init data structure
        dataStructure = this.initDataStructure(dataStructure);

        // Import data from json files

        final String[] filenamesUsers = new String[] {
                "/extra/small/users.json",
                "/extra/medium/users.json",
                "/extra/large/users.json",
        };

        final String[] filenamesPosts = new String[] {
                "/extra/small/posts.json",
                "/extra/medium/posts.json",
                "/extra/large/posts.json",
        };

        int[] sizes = new int[filenamesUsers.length];

        for (int i = 0; i < filenamesUsers.length; i++) {
            // Import data to arrays (they will be discarded by java garbage collector
            // after adding all the elements into the data structures)

            User[] users = null;
            try {
                users = JsonReader.parseUsers(filenamesUsers[i]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            sizes[i] = users.length;

            Post[] posts = null;
            try {
                posts = JsonReader.parsePosts(filenamesPosts[i]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Graph graph = (Graph) dataStructure;

            // Store users into hash table
            for (User user: users) {
                graph.getUsersByUsername().insert(user);
            }

            // Compute Graph
            graph.computeInitialGraph(users, posts);

            // Start timer
            Timer timer = new Timer();
            timer.triggerStart();

            // Remove each user from graph
            for (User user: users) {
                graph.removeFromGraph(user);

                // Remove posts from user
                datastructures.LinkedList.LinkedList<Post> userPosts = user.getPosts();
                Post[] toArray = userPosts.toArray(new Post[userPosts.getSize()]);
                for (Post post: toArray) {
                    graph.removeFromGraph(post);
                }
            }

            timer.triggerEnd();
            csvPrinter.getTimes().get(0).add(timer.computeDuration());

            System.out.println("Dataset " + filenamesUsers[i] + " in " + timer.computeFormattedDuration() + " (" + users.length + " users)");
        }

        csvPrinter.setnOfElements(sizes);

        csvPrinter.print(filename5Graph);
    }

    private Object initDataStructure(Object dataStructure) {

        // Options:
        //new Trie(),// Trie
        //new RTree(),// RTree
        //new AVLTree(),// AVLTree
        //new HashTable<SimpleElementWithStringKey>(),// HashTable
        //new Graph(),// Graph
        //new datastructures.LinkedList.LinkedList(),// LinkedList

        if (dataStructure instanceof Trie) {
            return new Trie();
        }
        else if (dataStructure instanceof RTree) {
            return new RTree();
        }
        else if (dataStructure instanceof AVLTree) {
            return new AVLTree();
        }
        else if (dataStructure instanceof HashTable) {
            return new HashTable<SimpleElementWithStringKey>();
        }
        else if (dataStructure instanceof Graph) {
            return new Graph();
        }
        else if (dataStructure instanceof datastructures.LinkedList.LinkedList) {
            return new datastructures.LinkedList.LinkedList();
        }
        return null;
    }

    private void registerDataStructure(Object dataStructure, CSVPrinter csvPrinter) {
        if (dataStructure instanceof Trie) {
            System.out.println("Data structure: " + Trie.DATA_STRUCTURE_NAME);
            csvPrinter.getNames().add(Trie.DATA_STRUCTURE_NAME);
            csvPrinter.getTimes().add(new LinkedList<>());
        }
        else if (dataStructure instanceof RTree) {
            System.out.println("Data structure: " + RTree.DATA_STRUCTURE_NAME);
            csvPrinter.getNames().add(RTree.DATA_STRUCTURE_NAME);
            csvPrinter.getTimes().add(new LinkedList<>());
        }
        else if (dataStructure instanceof AVLTree) {
            AVLTree avlTree = ((AVLTree) dataStructure);
            System.out.println("Data structure: " + AVLTree.DATA_STRUCTURE_NAME);
            csvPrinter.getNames().add(AVLTree.DATA_STRUCTURE_NAME);
            csvPrinter.getTimes().add(new LinkedList<>());
        }
        else if (dataStructure instanceof HashTable) {
            System.out.println("Data structure: " + HashTable.DATA_STRUCTURE_NAME);
            csvPrinter.getNames().add(HashTable.DATA_STRUCTURE_NAME);
            csvPrinter.getTimes().add(new LinkedList<>());
        }
        else if (dataStructure instanceof Graph) {
            System.out.println("Data structure: " + Graph.DATA_STRUCTURE_NAME);
            csvPrinter.getNames().add(Graph.DATA_STRUCTURE_NAME);
            csvPrinter.getTimes().add(new LinkedList<>());
        }
        else if (dataStructure instanceof datastructures.LinkedList.LinkedList) {
            datastructures.LinkedList.LinkedList linkedList = (datastructures.LinkedList.LinkedList) dataStructure;
            System.out.println("Data structure: " + datastructures.LinkedList.LinkedList.DATA_STRUCTURE_NAME);
            csvPrinter.getNames().add(datastructures.LinkedList.LinkedList.DATA_STRUCTURE_NAME);
            csvPrinter.getTimes().add(new LinkedList<>());
        }
    }

    private void printDataStructure(Object dataStructure) {
        if (dataStructure instanceof AVLTree) {
            AVLTree avlTree = ((AVLTree) dataStructure);
            TreePrinter treePrinter = new TreePrinter();
            try {
                treePrinter.printAVLTree(avlTree);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (dataStructure instanceof datastructures.LinkedList.LinkedList) {
            datastructures.LinkedList.LinkedList linkedList = (datastructures.LinkedList.LinkedList) dataStructure;
        }
    }

    private Object[] computeElements(Object dataStructure, int size) {

        // Options:
        //new Trie(),// Trie
        //new RTree(),// RTree
        //new AVLTree(),// AVLTree
        //new HashTable<SimpleElementWithStringKey>(),// HashTable
        //new Graph(),// Graph
        //new datastructures.LinkedList.LinkedList(),// LinkedList

        if (dataStructure instanceof Trie) {
            return ObjectFactory.computeUsersWithRandomUsername(size);
        }
        else if (dataStructure instanceof RTree) {
            RTree rTree = (RTree) dataStructure;

            // Compute random keys
            int[] keys = IntegerUtilities.generateRandomArrayWithNoDuplicates(size);

            // Generate posts using generated keys
            int keysLength = keys.length;
            Post[] posts = new Post[keysLength];
            for (int i = 0; i < keysLength; i++) {
                posts[i] = new Post();
                posts[i].fillWithRandomGeographicCoordinates();
            }

            return posts;
        }
        else if (dataStructure instanceof AVLTree || dataStructure instanceof datastructures.LinkedList.LinkedList) {

            // Compute random keys
            int[] keys = IntegerUtilities.generateRandomArrayWithNoDuplicates(size);
            //System.out.println(Arrays.toString(keys));

            // Generate elements using generated keys
            int keysLength = keys.length;
            SimpleElementWithIntegerKey[] elements = new SimpleElementWithIntegerKey[keysLength];
            for (int i = 0; i < keysLength; i++) elements[i] = new SimpleElementWithIntegerKey(keys[i]);

            return elements;
        }
        if (dataStructure instanceof HashTable) {
            return ObjectFactory.computeHashtagsWithRandomNames(size);
        }
        else if (dataStructure instanceof Graph) {
            return new Object[0];
        }
        return null;
    }

    private void insert(Object dataStructure, Object elementToInsert) {
        if (dataStructure instanceof Trie) {
            String username = ((User) elementToInsert).getUsername();
            ((Trie) dataStructure).addUser((User) elementToInsert);
        }
        else if (dataStructure instanceof RTree) {
            RTree rTree = (RTree) dataStructure;
            Post post = (Post) elementToInsert;
            rTree.addPost(post);
        }
        else if (dataStructure instanceof AVLTree) {
            ((AVLTree) dataStructure).insert((ElementWithIntegerKey) elementToInsert);
        }
        else if (dataStructure instanceof HashTable) {
            ((HashTable) dataStructure).insert(elementToInsert);
        }
        else if (dataStructure instanceof Graph) {
            if (elementToInsert instanceof User) {
                ((Graph) dataStructure).computeGraph((User) elementToInsert);
            }
            else if (elementToInsert instanceof Post) {
                ((Graph) dataStructure).computeGraph((Post) elementToInsert);
            }
        }
        else if (dataStructure instanceof datastructures.LinkedList.LinkedList) {
            ((datastructures.LinkedList.LinkedList) dataStructure).add((SimpleElementWithIntegerKey) elementToInsert);
        }
    }

    private void delete(Object dataStructure, Object elementToDelete) {
        if (dataStructure instanceof Trie) {
            String username = ((User) elementToDelete).getUsername();
            ((Trie) dataStructure).deleteUser(username);
        }
        else if (dataStructure instanceof RTree) {
            RTree rTree = (RTree) dataStructure;
            Post post = (Post) elementToDelete;
            rTree.removePost(post);
        }
        else if (dataStructure instanceof AVLTree) {
            ((AVLTree) dataStructure).deleteNode(((ElementWithIntegerKey) elementToDelete).getKey());
        }
        else if (dataStructure instanceof HashTable) {
            ((HashTable) dataStructure).remove(((ElementWithStringKey) elementToDelete).getKey());
        }
        else if (dataStructure instanceof Graph) {
            if (elementToDelete instanceof User) {
                ((Graph) dataStructure).removeFromGraph((User) elementToDelete);
            }
            else if (elementToDelete instanceof Post) {
                ((Graph) dataStructure).removeFromGraph((Post) elementToDelete);
            }
        }
        else if (dataStructure instanceof datastructures.LinkedList.LinkedList) {
            ((datastructures.LinkedList.LinkedList) dataStructure).removeByIntegerKey(((SimpleElementWithIntegerKey) elementToDelete).getKey());
        }
    }

    private void search(Object dataStructure, Object elementToSearch) {
        if (dataStructure instanceof Trie) {
            ((Trie) dataStructure).getSuggestions(((User) elementToSearch).getUsername());
        }
        else if (dataStructure instanceof RTree) {
            RTree rTree = (RTree) dataStructure;
            Post post = (Post) elementToSearch;
            rTree.getPost(post.getLocation());
        }
        else if (dataStructure instanceof AVLTree) {
            ((AVLTree) dataStructure).findNodeWithKey(((ElementWithIntegerKey) elementToSearch).getKey());
        }
        else if (dataStructure instanceof HashTable) {
            ((HashTable) dataStructure).get(((ElementWithStringKey) elementToSearch).getKey());
        }
        else if (dataStructure instanceof datastructures.LinkedList.LinkedList) {
            ((datastructures.LinkedList.LinkedList) dataStructure).getByIntegerKey(((SimpleElementWithIntegerKey) elementToSearch).getKey());
        }
    }
}