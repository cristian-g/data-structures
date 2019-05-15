package datastructures.RTree;

import datastructures.ElementWithCoordinates;
import models.Post;

public class RTree {
    // TODO: Change "ARRAY_SIZE" to a more meaningful name
    public static final int ARRAY_SIZE = 4;

    private Node root;

    public RTree() {
        root = new LeafNode(null);
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public void addPost(Post post) {
        addPost(post, root);
    }

    public void addPost(Post post, Node nextNode) {
        double[] postLocation = post.getLocation();

        while (!(nextNode instanceof LeafNode)) {
            Node[] child = ((InternalNode) nextNode).getChild();

            for (Node n : child) {
                if (postInTheRegion(n.getStart(), n.getEnd(), postLocation)) {
                    if (n instanceof LeafNode) {
                        if (n.isFull()) {
                            // TODO: fer el split
                            System.out.println("Percal incoming. Post in the region but is full!");
                        } else {
                            // We can add the post, do it.
                            ((LeafNode) n).addPost(post);
                        }
                    } else if (n instanceof InternalNode){
                        addPost(post, n);
                    }
                    return;
                } else {
                    // TODO: Calcular increment area minima
                    System.out.println("Ens espavilem");
                }
            }
        }

        if (nextNode.isFull()) {
            System.out.println("L'hem trobat pero esta ple");
        } else {
            ((LeafNode) nextNode).addPost(post);
        }
    }

    private static double calculaIncrement(double[] start, double[] end, double[] location) {
        double[] startClone = start.clone();
        double[] endClone = end.clone();
        double areaInicial = (endClone[0] - startClone[0]) * (endClone[1] - startClone[1]);

        if (location[0] > endClone[0]) {
            endClone[0] = location[0];
        }

        if (location[1] > endClone[1]) {
            endClone[1] = location[1];
        }

        if (location[0] < startClone[0]) {
            startClone[0] = location[0];
        }

        if (location[1] < startClone[1]) {
            startClone[1] = location[1];
        }

        double areaFinal = (endClone[0] - startClone[0]) * (endClone[1] - startClone[1]);

        return areaFinal - areaInicial;
    }

    private static double calculaArea(double[] p1, double[] p2) {
        return (p2[0] - p1[0]) * (p2[1] - p1[1]);
    }

    // Returns the post on that specific location
    public Post getPost(double[] location) {
        return null; // TODO: implement this function
    }

    // Returns the posts inside that region
    public Post[] getPosts(double[] corners) {
        return null; // TODO: implement this function
    }

    public void removePost(ElementWithCoordinates post) {
        Node nextNode = root;
        while (nextNode instanceof InternalNode) {
            Node[] child = ((InternalNode) nextNode).getChild();
            for (Node n : child) {
                if (n instanceof LeafNode) {
                    ((LeafNode) n).removePost((Post)post);
                } else if (n instanceof InternalNode){
                    //Mira si els punts estan dins la regio:
                    if(postInTheRegion(n.getStart(), n.getEnd(), post.getLocation())) {
                        nextNode = n;
                    }
                }
            }
        }
    }

    private static boolean postInTheRegion(double[] start, double[] end, double[] location) {
        if (start[0] <= location[0] && start[1] <= location[1]) {
            if (location[0] <= end[0] && location[1] <= end[1]) {
                return true;
            }
        }

        return false;
    }
}