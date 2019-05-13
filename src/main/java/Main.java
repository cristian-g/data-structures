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


        LinkedList<String> suggestions = t.getSuggestions("f");
        String[] sugg = suggestions.toArray(new String[suggestions.getSize()]);
        System.out.println("\n f");
        for(String s: sugg) {
            System.out.println("\t" + s);
        }

        suggestions = t.getSuggestions("hol");
        sugg = suggestions.toArray(new String[suggestions.getSize()]);
        System.out.println("\n hol");
        for(String s: sugg) {
            System.out.println("\t" + s);
        }

        suggestions = t.getSuggestions("perni");
        sugg = suggestions.toArray(new String[suggestions.getSize()]);
        System.out.println("\n perni");
        for(String s: sugg) {
            System.out.println("\t" + s);
        }


        t.deleteUser("sushi");
        t.deleteUser("perni");
        t.deleteUser("pernil");

        t.getSuggestions("s");

        TreePrinter tp = new TreePrinter();
        tp.printTrie(t);
        //Tests end.

        InstaSalle instasalle = new InstaSalle();
        instasalle.init();

    }
}