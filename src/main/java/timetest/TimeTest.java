package timetest;

import controller.Timer;
import datastructures.AVLTree.AVLTree;
import datastructures.ElementWithIntegerKey;
import test.SimpleElementWithIntegerKey;
import utils.IntegerUtilities;
import utils.print.CSVPrinter;
import utils.print.TreePrinter;

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

        int[] sizes = new int[5];
        int length = sizes.length;
        for (int i = 0; i < length; i++) sizes[i] = (int) Math.pow(10, i+1);

        System.out.println("\n" + "--------------------");
        System.out.println("Starting the test... We will try collections of the following sizes:");
        System.out.println(Arrays.toString(sizes));
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

            for (int size: sizes) {

                // Init data structure
                dataStructure = this.initDataStructure(dataStructure);

                // Compute random keys
                int[] keys = IntegerUtilities.generateRandomArrayWithNoDuplicates(size);

                // Generate elements using generated keys
                int keysLength = keys.length;
                SimpleElementWithIntegerKey[] elements = new SimpleElementWithIntegerKey[keysLength];
                for (int i = 0; i < keysLength; i++) elements[i] = new SimpleElementWithIntegerKey(keys[i]);

                // Insert elements into the data structure
                Timer timer = new Timer();
                timer.triggerStart();
                for (SimpleElementWithIntegerKey element: elements) {
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

            // Compute random keys
            int[] keys = IntegerUtilities.generateRandomArrayWithNoDuplicates(maxSize);
            //System.out.println(Arrays.toString(keys));

            // Generate elements using generated keys
            int keysLength = keys.length;
            SimpleElementWithIntegerKey[] elements = new SimpleElementWithIntegerKey[keysLength];
            for (int i = 0; i < keysLength; i++) elements[i] = new SimpleElementWithIntegerKey(keys[i]);

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
        if (dataStructure instanceof AVLTree) {
            return new AVLTree();
        }
        else if (dataStructure instanceof datastructures.LinkedList.LinkedList) {
            return new datastructures.LinkedList.LinkedList();
        }
        return null;
    }

    private void registerDataStructure(Object dataStructure, CSVPrinter csvPrinter) {
        if (dataStructure instanceof AVLTree) {
            AVLTree avlTree = ((AVLTree) dataStructure);
            csvPrinter.getNames().add(AVLTree.DATA_STRUCTURE_NAME);
            csvPrinter.getTimes().add(new LinkedList<>());
        }
        else if (dataStructure instanceof datastructures.LinkedList.LinkedList) {
            datastructures.LinkedList.LinkedList linkedList = (datastructures.LinkedList.LinkedList) dataStructure;
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

    private void insert(Object dataStructure, Object elementToInsert) {
        if (dataStructure instanceof AVLTree) {
            ((AVLTree) dataStructure).insert((ElementWithIntegerKey) elementToInsert);
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