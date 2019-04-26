package test;

import datastructures.AVLTree.AVLTree;
import datastructures.ElementWithIntegerKey;
import utils.IntegerUtilities;
import utils.print.TreePrinter;

import java.util.Arrays;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class Test {
    @org.junit.jupiter.api.Test
    void AVLTreeinOrder() {
        int[] sizes = new int[1];
        int length = sizes.length;
        for (int i = 0; i < length; i++) sizes[i] = (int) Math.pow(100, i+1);

        System.out.println("--------------------");
        System.out.println("Starting the test. We will try collections of the following sizes:");
        System.out.println("--------------------");
        System.out.println(Arrays.toString(sizes));

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
            for (SimpleElementWithIntegerKey element: elements) {
                tree.setRoot(tree.insert(tree.getRoot(), element));
                TreePrinter treePrinter = new TreePrinter();
                treePrinter.printGraph(tree.getRoot());
            }

            // Retrieve elements in order from data structure
            ElementWithIntegerKey[] inOrder = tree.inOrder(tree.getRoot());

            // Using JAVA TreeSet: sort original elements and remove elements with same key
            TreeSet<SimpleElementWithIntegerKey> treeJava = new TreeSet<SimpleElementWithIntegerKey>();
            for(int i = 0; i < keysLength; i++) treeJava.add(elements[i]);
            SimpleElementWithIntegerKey[] sortedByJava = new SimpleElementWithIntegerKey[treeJava.size()];
            treeJava.toArray(sortedByJava);

            // Print status
            System.out.println("sortedByJava length: " + sortedByJava.length);
            //System.out.println("sortedByJava: " + SimpleElementWithIntegerKey.arrayToString(sortedByJava));
            System.out.println("inOrder length: " + inOrder.length);
            //System.out.println("inOrder: " + SimpleElementWithIntegerKey.arrayToString(inOrder));

            // Check if retrieved elements are in the same order
            assertArrayEquals(sortedByJava, inOrder);

            // Delete node
            tree.setRoot(tree.deleteNode(tree.getRoot(), elements[2].getKey()));

            // Retrieve elements in order from data structure
            ElementWithIntegerKey[] inOrder2 = tree.inOrder(tree.getRoot());

            treeJava.remove(elements[2]);
            SimpleElementWithIntegerKey[] sortedByJava2 = new SimpleElementWithIntegerKey[treeJava.size()];
            treeJava.toArray(sortedByJava2);

            System.out.println("sortedByJava2 length: " + sortedByJava2.length);
            System.out.println("inOrder length: " + inOrder2.length);

            // Check if retrieved elements are in the same order
            assertArrayEquals(sortedByJava2, inOrder2);
        }
    }
}