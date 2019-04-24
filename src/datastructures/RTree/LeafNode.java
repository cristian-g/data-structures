package datastructures.RTree;

import models.Post;

import static datastructures.RTree.RTree.ARRAY_SIZE;

public class LeafNode extends Node {
    private Post[] posts;

    public LeafNode() {
        super();
        this.posts = new Post[ARRAY_SIZE];
    }

    // TODO: implement this function if we need it
    public void addPost(Node node, int position) {

    }

    // TODO: implement this function if we need it
    public Node getPost(int position) {
        return null;
    }

    // TODO: implement this function if we need it
    public void removePost(int position) {

    }
}
