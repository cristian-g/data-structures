package utils;

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

    public static int[] generateRandomArray(int n){
        int[] array = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) array[i] = random.nextInt(100000);
        return array;
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
