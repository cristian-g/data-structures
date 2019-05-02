package datastructures.RTree;

import datastructures.ElementWithCoordinates;
import models.Post;

public class RTree {
    // TODO: Change "ARRAY_SIZE" to a more meaningful name
    public static final int ARRAY_SIZE = 3;

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
        Node nextNode = root;

        while (nextNode instanceof InternalNode) {
            Node[] child = ((InternalNode) nextNode).getChild();

            for (Node n : child) {
                if (n instanceof LeafNode) {

                } else if (n instanceof InternalNode){

                }
            }
        }

        assert (nextNode instanceof LeafNode);
        if (nextNode.isFull()) {

        } else {
            ((LeafNode) nextNode).addPost(post);
        }
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

    private boolean postInTheRegion(double[] start, double[] end, double[] location) {
        //Si està dins del marge de les x:
        if((location[0] > start[0] && location[0] < end[0]) || (location[0] > end[0] && location[0] < start[0])) {
            //Si està dins el marge de les y:
            if((location[1] > start[1] && location[1] < end[1]) ||(location[1] > end[1] && location[1] < start[1])) {
                return  true;
            }
        }
        return false;
    }
}