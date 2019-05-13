import controller.InstaSalle;
import datastructures.LinkedList.LinkedList;
import datastructures.Trie.Trie;
import utils.print.TreePrinter;

public class Main {

    public static void main(String[] args) {
        //Tests begin:
        Trie t = new Trie();
        t.addUser("ferran");
        t.addUser("ferro");
        t.addUser("iscle");
        t.addUser("cristian");
        t.addUser("test");
        t.addUser("fita");
        t.addUser("sushi");
        t.addUser("maki");
        t.addUser("paed");
        t.addUser("pernia");
        t.addUser("pernil");
        t.addUser("perni");
        t.addUser("pernilet");
        LinkedList<String> l = new LinkedList<>();
        l.insert("hola");
        l.insert("adeu");
        t.addAllUsers(l);

        t.getSuggestions("hol");
        System.out.println("---");
        t.getSuggestions("f");
        System.out.println("---");
        t.getSuggestions("s");

        t.deleteUser("sushi");
        t.deleteUser("perni");
        t.deleteUser("pernil");

        TreePrinter tp = new TreePrinter();
        tp.printTrie(t);
        //Tests end.

        InstaSalle instasalle = new InstaSalle();
        instasalle.init();

    }
}