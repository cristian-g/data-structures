package timetest;

import controller.Timer;
import datastructures.AVLTree.AVLTree;
import datastructures.ElementWithIntegerKey;
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
    public final String filename2 = "insertion_1_element_in_n_elements";// Insertion of an element having n elements
    public final String filename3 = "search_1_element_in_n_elements";// Search for an element having n elements
    public final String filename4 = "elimination_1_element_in_n_elements";// Elimination of an element having n elements
    public final String filename5 = "elimination_n_elements";// Elimination of n elements (until leaving empty structure)

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
                //new RTree(),// RTree
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
                System.out.println("Size: " + size);

                // Init data structure
                dataStructure = this.initDataStructure(dataStructure);

                if (dataStructure instanceof Graph) {
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

                    // Import data to arrays (they will be discarded by java garbage collector
                    // after adding all the elements into the data structures)

                    User[] users = null;
                    try {
                        users = JsonReader.parseUsers(filenamesUsers[0]);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Post[] posts = null;
                    try {
                        posts = JsonReader.parsePosts(filenamesPosts[0]);
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

                    csvPrinter.getTimes().get(count).add(timer.computeDuration());
                }
                else {
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
            }
            count++;
        }

        csvPrinter.setnOfElements(sizes);

        csvPrinter.print(filename1);
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
                new datastructures.LinkedList.LinkedList(),// LinkedList
                new AVLTree(),// AVLTree
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
            ((Trie) dataStructure).addUser((User) elementToInsert);
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

    public void runTimeTest3() {
        CSVPrinter csvPrinter = new CSVPrinter();
        csvPrinter.print(filename3);
    }

    public void runTimeTest4() {
        CSVPrinter csvPrinter = new CSVPrinter();
        csvPrinter.print(filename4);
    }

    public void runTimeTest5() {
        CSVPrinter csvPrinter = new CSVPrinter();
        csvPrinter.print(filename5);
    }
}