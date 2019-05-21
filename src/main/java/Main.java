import controller.InstaSalle;
import timetest.TimeTest;
import datastructures.LinkedList.LinkedList;
import datastructures.RTree.RTree;
import models.Post;
import utils.print.TreePrinter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {



        TreePrinter treePrinter = new TreePrinter();
        try {
            treePrinter.printTrie(TreePrinter.initTrieExample2());
        } catch (IOException e) {
            e.printStackTrace();
        }


        InstaSalle instasalle = new InstaSalle();
        instasalle.init();

        /* TESTS BEGIN
        try {
            RTree rt = RTree.getTestRTree();
            Post p = new Post(10, new double[] {1,2});
            rt.addPost(p);
            rt.removePost(new double[] {6,6}, rt.getRoot());

            new TreePrinter().printRTree(rt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TESTS END */

    }
}