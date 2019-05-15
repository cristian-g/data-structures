package timetest;

import controller.Timer;
import datastructures.AVLTree.AVLTree;
import test.SimpleElementWithIntegerKey;
import utils.IntegerUtilities;
import utils.print.CSVPrinter;

import java.util.Arrays;
import java.util.LinkedList;

public class TimeTest {

    public void runTimeTest1() {

        CSVPrinter csvPrinter = new CSVPrinter();

        int[] sizes = new int[5];
        int length = sizes.length;
        for (int i = 0; i < length; i++) sizes[i] = (int) Math.pow(10, i+1);

        System.out.println("\n" + "--------------------");
        System.out.println("Starting the test. We will try collections of the following sizes:");
        System.out.println(Arrays.toString(sizes));
        System.out.println("--------------------" + "\n");

        // -------------------------------------
        // LinkedList
        csvPrinter.getNames().add(datastructures.LinkedList.LinkedList.DATA_STRUCTURE_NAME);
        csvPrinter.getTimes().add(new LinkedList<>());
        // -------------------------------------

        for (int size: sizes) {

            // Init data structure
            datastructures.LinkedList.LinkedList linkedList = new datastructures.LinkedList.LinkedList();

            // Compute random keys
            int[] keys = IntegerUtilities.generateRandomArray(size);

            // Generate elements using generated keys
            int keysLength = keys.length;
            SimpleElementWithIntegerKey[] elements = new SimpleElementWithIntegerKey[keysLength];
            for (int i = 0; i < keysLength; i++) elements[i] = new SimpleElementWithIntegerKey(keys[i]);

            // Insert elements into the data structure
            Timer timer = new Timer();
            timer.triggerStart();
            for (SimpleElementWithIntegerKey element: elements) {
                linkedList.add(element);
            }
            timer.triggerEnd();

            csvPrinter.getTimes().get(0).add(timer.computeDuration());
        }

        // -------------------------------------
        // AVLTree
        csvPrinter.getNames().add(AVLTree.DATA_STRUCTURE_NAME);
        csvPrinter.getTimes().add(new LinkedList<>());
        // -------------------------------------

        for (int size: sizes) {

            // Init data structure
            AVLTree tree = new AVLTree();

            // Compute random keys
            int[] keys = IntegerUtilities.generateRandomArray(size);

            // Generate elements using generated keys
            int keysLength = keys.length;
            SimpleElementWithIntegerKey[] elements = new SimpleElementWithIntegerKey[keysLength];
            for (int i = 0; i < keysLength; i++) elements[i] = new SimpleElementWithIntegerKey(keys[i]);

            // Insert elements into the data structure
            Timer timer = new Timer();
            timer.triggerStart();
            for (SimpleElementWithIntegerKey element: elements) {
                tree.insert(element);
            }
            timer.triggerEnd();

            csvPrinter.getTimes().get(1).add(timer.computeDuration());
        }

        csvPrinter.setnOfElements(sizes);

        csvPrinter.print();
    }
}
