import controller.InstaSalle;
import datastructures.LinkedList.LinkedList;
import datastructures.RTree.RTree;
import models.Post;
import utils.print.TreePrinter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        InstaSalle instasalle = new InstaSalle();
        instasalle.init();

        /* TESTS BEGIN
        try {
            RTree rt = RTree.getTestRTree();
            Post p = new Post(10, new double[] {7,7});
            //rt.addPost(p);
            Post p2 = rt.getPost(new double[] {7,7});
            System.out.println(p == p2);
            System.out.println(p2 == null);

            LinkedList<Post> posts = rt.getPosts(new double[] {4,4}, new double[]{7,7}, rt.getRoot());
            Post[] postsArr = posts.toArray(new Post[posts.getSize()]);
            for(Post po: postsArr) {
                System.out.println(po.getId());
            }

            new TreePrinter().printRTree(rt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TESTS END */

    }
}