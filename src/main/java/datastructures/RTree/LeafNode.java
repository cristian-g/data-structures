package datastructures.RTree;

import models.Post;

import java.util.Arrays;

import static datastructures.RTree.RTree.MAX_ITEMS;
import static datastructures.RTree.RTree.MIN_ITEMS;

public class LeafNode extends Node {
    private Post[] posts;

    public LeafNode(RTree tree) {
        super(tree);
        this.posts = new Post[MAX_ITEMS];
    }

    public void setPosts(Post[] posts) {
        this.posts = posts;
        findNewLimits();
    }

    public void addPost(Post post) {
        if (isFull()) {
            split(post);
        } else {
            posts[length++] = post;

            if (length == 1) {
                double[] location = post.getLocation();
                start[0] = location[0];
                start[1] = location[1];
                end[0] = location[0];
                end[1] = location[1];
            }
        }

        findNewLimits();
        updateRegions();
    }

    public void split(Post p) {
        Post[] postsToAdd = Arrays.copyOf(getPosts(), getLength() + 1);
        postsToAdd[getLength()] = p;

        // Troba els 2 posts mes allunyats entre ells:
        Post[] furthestPosts = findFurthestPosts(postsToAdd);

        // Ara ja tenim els 2 posts mes allunyats entre ells.
        // Creem 2 regions i 2 leafNodes i inserim un post a cada leafNode:
        Post[] tmpPosts = getPosts(); // Aixi no hem de tornar a crear un array amb max size
        tmpPosts[0] = furthestPosts[0];
        setPosts(tmpPosts);
        setLength(1);

        LeafNode newLeafNode = new LeafNode(tree);
        newLeafNode.addPost(furthestPosts[1]);
        if (getParent() == null) {
            InternalNode newRoot = new InternalNode(tree);
            setParent(newRoot);
            tree.setRoot(newRoot);
        }
        ((InternalNode) getParent()).addChild(newLeafNode);

        // Ara que ja tenim els 2 posts en les seves regions, s'han d'afegir la resta de posts entre la R1 i la R2:
        addRemainingPosts(newLeafNode, postsToAdd, furthestPosts);
    }

    //Retorna la distancia al quadrat que hi ha entre post1 i post2:
    public double calculateDistance(Post post1, Post post2) {
        double[] locationP1 = post1.getLocation();
        double[] locationP2 = post2.getLocation();
        return (Math.pow(locationP1[0] - locationP2[0], 2) + Math.pow(locationP1[1] - locationP2[1], 2));
    }

    // Retorna els dos posts mes allunyats entre ells:
    public Post[] findFurthestPosts(Post[] postsArr) {
        double max = 0;
        Post[] posts = new Post[2];
        for (int i = 0; i < postsArr.length; i++) {
            for (int j = i + 1; j < postsArr.length; j++) {
                double dist = calculateDistance(postsArr[i], postsArr[j]);
                if (dist > max) {
                    max = dist;
                    posts[0] = postsArr[i];
                    posts[1] = postsArr[j];
                }
            }
        }
        return posts;
    }

    //Mira a quina de les dues regions hi hauria increment d'area mes petit per a inserir el post en aquella regio:
    public boolean findNearestRegion(LeafNode leaf1, LeafNode leaf2, Post p) {
        double inc1 = calculaIncrement(leaf1.getStart(), leaf1.getEnd(), p.getLocation());
        double inc2 = calculaIncrement(leaf2.getStart(), leaf2.getEnd(), p.getLocation());
        return inc1 > inc2;
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

    public void addRemainingPosts(LeafNode l2, Post[] posts, Post[] furthestPosts) {
        int postsToAdd = posts.length - 2;
        for (Post p : posts) {
            if (p != furthestPosts[0] && p != furthestPosts[1]) {
                if (MIN_ITEMS - getLength() == postsToAdd) {
                    addPost(p);
                } else if (MIN_ITEMS - l2.getLength() == postsToAdd) {
                    l2.addPost(p);
                } else {
                    if (findNearestRegion(this, l2, p)) {
                        addPost(p);
                    } else {
                        l2.addPost(p);
                    }
                }
                postsToAdd--;
            }
        }
    }

    public void updateRegions() {
        Node tmpParent = getParent();

        while (tmpParent != null) {
            double[] startParent = tmpParent.getStart();
            double[] endParent = tmpParent.getEnd();

            if (endParent[0] < end[0]) {
                endParent[0] = end[0];
            }

            if (endParent[1] < end[1]) {
                endParent[1] = end[1];
            }

            if (startParent[0] > start[0]) {
                startParent[0] = start[0];
            }

            if (startParent[1] > start[1]) {
                startParent[1] = start[1];
            }

            tmpParent = tmpParent.getParent();
        }
    }

    //Get post by reference:
    public Post getPost(Post post) {
        for (Post p : posts) {
            if (p != null && p.getLocation()[0] == post.getLocation()[0] && p.getLocation()[1] == post.getLocation()[1]) {
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
        if (p != null) {
            p.setInvisible();
        }
    }

    //Get post by location:
    public Post getPost(double[] postLocation) {
        for (Post p : posts) {
            if (p != null && p.getLocation()[0] == postLocation[0] && p.getLocation()[1] == postLocation[1]) {
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
        if (p != null) {
            p.setInvisible();
        }
    }

    public void findNewLimits() {
        //Actualitzem límits del node actual:
        for (Post p : posts) {
            if (p != null) {
                double[] postLocation = p.getLocation();
                end = new double[]{0, 0};
                start = new double[]{0, 0};

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
