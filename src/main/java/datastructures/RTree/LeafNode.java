package datastructures.RTree;

import models.Post;

import static datastructures.RTree.RTree.MAX_ITEMS;

public class LeafNode extends Node {
    private Post[] posts;

    public LeafNode(Node parent) {
        super(parent);
        this.posts = new Post[MAX_ITEMS];
    }

    public void setPosts(Post[] posts) {
        this.posts = posts;
    }

    //TODO: call setVisible when you add a Post
    public void addPost(Post post) {
        assert (!isFull()); // We can only add if we have space left in the array

        double[] postLocation = post.getLocation(); // Get the location of the post we want to add
        for (int i = 0; i < length; i++) {
            double[] location = posts[i].getLocation(); // Get the location of the post in the current array position
            if (location[0] > postLocation[0] && location[1] > postLocation[1]) { // If x and y are bigger, do the insertion
                int position  = i;
                for (i = length; i > position; i--) {
                    posts[i] = posts[i - 1];
                }
                posts[position] = post;
                break; // Stop looking for more positions as we've already inserted it
            } else if (i == (length - 1)) { // If we've already tried all positions and none worked, do the insertion as the last item
                posts[length] = post;
            }
        }

        length++; // Increase the length of the node by 1
    }

    // TODO: implement this function if we need it
    public Post getPost(Post post) {
        for(Post p: posts) {
            if(p.equals(post)) {
                //If the post exists, return it.
                return p;
            }
        }
        //If the post doesn't exist, return null.
        return null;
    }

    // TODO: implement this function if we need it
    public void removePost(Post post) {
        Post p = getPost(post);
        p.setInvisible();
    }

    public Post[] getPosts() {
        return posts;
    }

    public void fillWithPostsWithRandomGeographicCoordinates(int emptyBoxes) {

        int desiredLength = RTree.MAX_ITEMS;

        Post[] posts = new Post[desiredLength];

        int boxesToFill = desiredLength - emptyBoxes;
        for (int i = 0; i < boxesToFill; i++) {
            Post post = new Post();
            post.fillWithRandomInfo();
            posts[i] = post;
        }

        this.setPosts(posts);
    }
}
