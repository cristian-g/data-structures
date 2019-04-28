package datastructures.RTree;

import models.Post;

public class RTree {
    // TODO: Change "ARRAY_SIZE" to a more meaningful name
    public static final int ARRAY_SIZE = 3;

    private Node root;

    public RTree() {
        root = new LeafNode(null);
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
}