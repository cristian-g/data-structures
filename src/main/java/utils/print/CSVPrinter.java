package utils.print;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.util.LinkedList;


public class CSVPrinter {
    private int[] nOfElements;
    private LinkedList<LinkedList<Double>> times;
    private LinkedList<String> names;

    private static int files_count = 0;

    public CSVPrinter() {
        this.times = new LinkedList<>();
        this.names = new LinkedList<>();
    }

    public int[] getnOfElements() {
        return nOfElements;
    }

    public void setnOfElements(int[] nOfElements) {
        this.nOfElements = nOfElements;
    }

    public LinkedList<LinkedList<Double>> getTimes() {
        return times;
    }

    public void setTimes(LinkedList<LinkedList<Double>> times) {
        this.times = times;
    }

    public LinkedList<String> getNames() {
        return names;
    }

    public void setNames(LinkedList<String> names) {
        this.names = names;
    }

    public void print() {
        
        try (PrintWriter writer = new PrintWriter(new File("out/time-tests/test" + (files_count + 1) + ".csv"))) {

            StringBuilder sb = new StringBuilder();

            // Headline
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                sb.append(';');
                sb.append(name);
            }

            for (int i = 0; i < nOfElements.length; i++) {
                sb.append('\n');

                int n = nOfElements[i];
                sb.append(n);
                sb.append(';');

                for (int j = 0; j < names.size(); j++) {
                    double time = times.get(j).get(i);
                    sb.append(time);
                    sb.append(';');
                }
            }

            writer.write(sb.toString());
            files_count++;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
