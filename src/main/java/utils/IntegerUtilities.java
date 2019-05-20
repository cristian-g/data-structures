package utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Useful functions to deal with integers.
 *
 * @author Cristian, Ferran, Iscle
 *
 */
public class IntegerUtilities {

    /**
     * A utility function to get maximum of two integers
     * @param a
     * @param b
     * @return the maximum of the two integers
     */
    public static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    public static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) array[i] = random.nextInt(100000);
        return array;
    }

    public static int[] generateRandomArrayWithNoDuplicates(int size) {
        int[] array = new int[size];

        ArrayList<Integer> list = new ArrayList<Integer>(size);
        for(int i = 1; i <= size; i++) {
            list.add(i);
        }

        Random rand = new Random();
        int count = 0;
        while(list.size() > 0) {
            int index = rand.nextInt(list.size());
            array[count] = list.remove(index);
            count++;
        }

        return array;
    }

    public static int[] generateCounterArray(int size) {
        int[] result = new int[size];
        for (int i = 0; i < size; i++) result[i] = i;
        return result;
    }

    public static int computeRandomId() {
        Random random = new Random();
        int randomId = random.nextInt(100000);
        return randomId;
    }

    public static int computeRandomIntegerBetween(int min, int max) {
        Random random = new Random();
        int randomNumber = random.nextInt(max + 1 - min) + min;
        return randomNumber;
    }
}
