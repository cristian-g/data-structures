package datastructures.RTree;

import models.Point;
import models.Post;

import static datastructures.RTree.RTree.ARRAY_SIZE;

public class LeafNode extends Node {
    private Post[] posts;

    public LeafNode() {
        super();
        this.posts = new Post[ARRAY_SIZE];
    }

    // TODO: implement this function if we need it
    public void addPost(Post post) {
        // TODO: find here the best position to insert the post
    }

    // TODO: implement this function if we need it
    public Post getPost(Point point) {
        return null;
    }

    // TODO: implement this function if we need it
    public void removePost(int position) {

    }
}
