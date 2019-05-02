import controller.InstaSalle;
import utils.print.TreePrinter;

public class Main {

    public static void main(String[] args) {
        TreePrinter treePrinter = new TreePrinter();
        treePrinter.printRTreeExample();

        InstaSalle instasalle = new InstaSalle();
        instasalle.init();
    }
}