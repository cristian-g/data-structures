package utils;

/**
 * Useful functions to deal with strings.
 *
 * @author Cristian, Ferran, Iscle
 *
 */
public class StringUtilities {
    /**
     * Add all the strings that are entered into the passed string builder as a parameter.
     *
     * @param sb    Where all the parameters will be added.
     * @param args  Un nombre variable de cadenes a afegir sobre sb.
     */
    public static void append(StringBuilder sb, String... args) {
        for (String s : args) sb.append(s);
    }

    /**
     * Add all the strings that are entered into the passed string builder as a parameter along with a line break.
     *
     * @param sb    Where all the parameters will be added.
     * @param args  A variable number of strings to add on sb.
     */
    public static void appendLines(StringBuilder sb, String... args) {
        for (String s : args) {
            sb.append(s);
            sb.append("\n");
        }
    }

    /**
     * Returns a string from the elements of an array.
     * Source: https://stackoverflow.com/questions/16773599/explode-and-implode-just-like-php-in-java
     * @param array
     * @return A string from the elements of an array.
     */
    public static String implode(Object[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i].toString());
            if (i != array.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static String createStringFromSeconds(long totalSecs) {
        long hours = totalSecs / 3600;
        long minutes = (totalSecs % 3600) / 60;
        long seconds = totalSecs % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void printTabbedLine(String string) {
        System.out.println(string.replace("\n", "\n\t"));
    }
}
