package datastructures.RTree;

import models.Point;
import models.Post;

public class RTree {
    // TODO: Change "ARRAY_SIZE" to a more meaningful name
    public static final int ARRAY_SIZE = 3;

    private Node root;

    public RTree() {
        root = new LeafNode();
    }

    public void addPost(Post post) {
        // TODO: implement this function
    }

    // Returns the post on that specific point
    public Post getPost(Point point) {
        return null; // TODO: implement this function
    }

    // Returns the posts inside that region
    public Post[] getPosts(Point[] corners) {
        return null; // TODO: implement this function
    }
}