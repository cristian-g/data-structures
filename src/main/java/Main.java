import controller.InstaSalle;
import datastructures.Trie.Trie;
import utils.print.TreePrinter;

public class Main {
    public static void main(String[] args) {

        Trie t = new Trie();
        t.addUser("hola");
        t.addUser("adeu");
        t.addUser("maki");
        t.addUser("sushi");
        t.addUser("perni");
        t.addUser("pernil");
        t.addUser("pernilet");
        t.addUser("pernia");

        t.getSuggestions("p");

        t.limitMemory(3);

        TreePrinter tp = new TreePrinter();
        tp.printTrie(t);

        InstaSalle instasalle = new InstaSalle();
        instasalle.init();
    }
}