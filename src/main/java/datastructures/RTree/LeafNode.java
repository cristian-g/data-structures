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
        if (isFull()) {
            System.out.println("No space left in the array!");
            return;
        }

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

        if (length == 0) {
            posts[0] = post;
            start[0] = postLocation[0];
            start[1] = postLocation[1];
            end[0] = postLocation[0];
            end[1] = postLocation[1];
        } else {
            if (postLocation[0] > end[0]) {
                end[0] = postLocation[0];
            }

            if (postLocation[1] > end[1]) {
                end[1] = postLocation[1];
            }

            if (postLocation[0] < start[0]) {
                start[0] = postLocation[0];
            }

            if (postLocation[1] < start[1]) {
                start[1] = postLocation[1];
            }
        }

        length++; // Increase the length of the node by 1
    }

    //Get post by reference:
    public Post getPost(Post post) {
        for(Post p: posts) {
            if(p != null && p.getLocation()[0] == post.getLocation()[0] && p.getLocation()[1] == post.getLocation()[1]) {
                //If the post exists, return it.
                return p;
            }
        }
        //If the post doesn't exist, return null.
        return null;
    }

    //Remove post by reference:
    public void removePost(Post post) {
        Post p = getPost(post);
        if(p != null) {
            p.setInvisible();
        }
    }

    //Get post by location:
    public Post getPost(double[] postLocation) {
        for(Post p: posts) {
            if(p != null && p.getLocation()[0] == postLocation[0] && p.getLocation()[1] == postLocation[1]) {
                //If the post exists, return it.
                return p;
            }
        }
        //If the post doesn't exist, return null.
        return null;
    }

    //Remove post by location:
    public void removePost(double[] postLocation) {
        Post p = getPost(postLocation);
        if(p != null) {
            p.setInvisible();
        }
    }

    public void findNewLimits() {
        //Actualitzem límits del node actual:
        for(Post p: posts) {
            if (p != null) {
                double[] postLocation = p.getLocation();
                end = new double[] {0, 0};
                start = new double[] {0, 0};

                if (postLocation[0] > end[0]) {
                    end[0] = postLocation[0];
                }

                if (postLocation[1] > end[1]) {
                    end[1] = postLocation[1];
                }

                if (postLocation[0] < start[0]) {
                    start[0] = postLocation[0];
                }

                if (postLocation[1] < start[1]) {
                    start[1] = postLocation[1];
                }
            }
        }
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
