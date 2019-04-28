package test;

import datastructures.AVLTree.AVLTree;
import datastructures.ElementWithIntegerKey;
import datastructures.LinkedList.LinkedList;
import utils.IntegerUtilities;
import utils.print.TreePrinter;

import java.util.Arrays;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class Test {

    @org.junit.jupiter.api.Test
    void AVLTreeinOrder() {
        int[] sizes = new int[5];
        int length = sizes.length;
        for (int i = 0; i < length; i++) sizes[i] = (int) Math.pow(10, i+1);

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
                tree.insert(element);
                //TreePrinter treePrinter = new TreePrinter();
                //treePrinter.printGraph(tree.getRoot());
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
            tree.deleteNode(elements[2].getKey());

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

    @org.junit.jupiter.api.Test
    void testUnsortedDataStructure() {
        int[] sizes = new int[5];
        int length = sizes.length;
        for (int i = 0; i < length; i++) sizes[i] = (int) Math.pow(10, i+1);

        System.out.println("--------------------");
        System.out.println("Starting the test. We will try collections of the following sizes:");
        System.out.println("--------------------");
        System.out.println(Arrays.toString(sizes));

        for (int size: sizes) {

            // Init data structure
            LinkedList dataStructure = new LinkedList();

            // Compute random keys
            int[] keys = IntegerUtilities.generateRandomArray(size);

            // Generate elements using generated keys
            int keysLength = keys.length;
            SimpleElementWithIntegerKey[] elements = new SimpleElementWithIntegerKey[keysLength];
            for (int i = 0; i < keysLength; i++) elements[i] = new SimpleElementWithIntegerKey(keys[i]);

            // Insert elements into the data structure
            for (SimpleElementWithIntegerKey element: elements) {
                dataStructure.insert(element);
            }

            // Using Java LinkedList
            java.util.LinkedList<SimpleElementWithIntegerKey> javaLinkedList = new java.util.LinkedList<SimpleElementWithIntegerKey>();
            for(int i = 0; i < keysLength; i++) javaLinkedList.add(elements[i]);

            // Assert
            assertEqualsCustom(dataStructure, javaLinkedList);

            // Delete node
            dataStructure.deleteByKey(elements[2].getKey());
            javaLinkedList.remove(new SimpleElementWithIntegerKey(elements[2].getKey()));

            // Assert
            assertEqualsCustom(dataStructure, javaLinkedList);

        }
    }

    private void assertEqualsCustom(LinkedList dataStructure, java.util.LinkedList<SimpleElementWithIntegerKey> javaLinkedList) {

        // Retrieve elements in an array from data structure
        ElementWithIntegerKey[] array = dataStructure.toArray();
        int keysLength = array.length;
        SimpleElementWithIntegerKey[] arrayByJava = javaLinkedList.toArray(new SimpleElementWithIntegerKey[javaLinkedList.size()]);

        // Print status
        System.out.println("LinkedListJava length: " + arrayByJava.length);
        System.out.println("dataStructure length: " + array.length);

        // Assert for size
        assertEquals(array.length, arrayByJava.length);

        // Iterate
        for (ElementWithIntegerKey element: arrayByJava) {
            boolean contains = dataStructure.contains(element.getKey());
            assertTrue(contains);
            dataStructure.deleteByKey(element.getKey());
        }

        // Regenerate data structure
        for(int i = 0; i < keysLength; i++) dataStructure.insert(array[i]);
    }
}