package datastructures.RTree;

import datastructures.LinkedList.LinkedList;
import models.Post;

import java.util.Arrays;

public class RTree {

    public static String DATA_STRUCTURE_NAME = "R-Tree";

    // TODO: Change "MAX_ITEMS" to a more meaningful name
    public static final int MAX_ITEMS = 3;
    public static final int MIN_ITEMS = MAX_ITEMS / 2 + MAX_ITEMS % 2;

    private Node root;

    public RTree() {
        root = new LeafNode(this);
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

    private void addPost(Post post, Node nextNode) {
        post.setVisible();
        // Life saver: http://www.mathcs.emory.edu/~cheung/Courses/554/Syllabus/3-index/R-tree.html
        if (nextNode instanceof InternalNode) {
            Node[] child = ((InternalNode) nextNode).getChild();

            // Provem a insertar el node a tots els child
            for (int i = 0; i < nextNode.length; i++) {
                Node n = child[i];
                if (postInTheRegion(n.getStart(), n.getEnd(), post.getLocation())) {
                    addPost(post, n);
                    return;
                }
            }

            // Si arribem aqui es que no hi ha cap node que el pugui agafar (perque no esta "inTheRegion")...
            // Ara hem de trobar el bounding rectangle a "nextNode" que augmenti area minima al insertar el post
            Node minimumAreaNode = child[0];
            double minimumAreaIncrease = calculaIncrement(child[0].start, child[0].end, post.getLocation());

            for (int i = 1; i < nextNode.length; i++) {
                Node n = child[i];
                double a = calculaIncrement(n.start, n.end, post.getLocation());
                if (a < minimumAreaIncrease) {
                    minimumAreaNode = n;
                    minimumAreaIncrease = a;
                }
            }

            // Quan trobem el que augmentaria minim l'area, fem crida recursiva per insertar el post a aquell node
            addPost(post, minimumAreaNode);
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

    public Post getPost(double[] location, Node nextNode) {
        if (nextNode != null && postInTheRegion(nextNode.getStart(), nextNode.getEnd(), location)) {
            if (nextNode instanceof InternalNode) {
                Node[] child = ((InternalNode) nextNode).getChild();

                for (Node n : child) {
                    Post tmp = getPost(location, n);

                    if (tmp == null) {
                        continue;
                    }

                    if (!tmp.isVisible()) return null;

                    return tmp;
                }
            } else if (nextNode instanceof LeafNode) {
                Post[] posts = ((LeafNode) nextNode).getPosts();

                for (Post p : posts) {
                    if (p != null && Arrays.equals(p.getLocation(), location) && p.isVisible()) {
                        return p;
                    }
                }
            }
        }

        return null;
    }

    // Returns the post on that specific location
    public Post getPost(double[] location) {
        return getPost(location, root);
    }

    public LinkedList<Post> getPosts(double[] start, double[] end) {
        return getPosts(start, end, root);
    }

    // Returns the posts inside that region
    public LinkedList<Post> getPosts(double[] start, double[] end, Node nextNode) {
        LinkedList<Post> posts = new LinkedList<>();

        if (nextNode instanceof InternalNode) {
            Node[] child = ((InternalNode) nextNode).getChild();

            for (int i = 0; i < nextNode.getLength(); i++) {
                Node n = child[i];
                if (regionIntersectsRegion(start, end, n.getStart(), n.getEnd())) {
                    LinkedList<Post> aux = getPosts(start, end, n);
                    Post[] auxArr = aux.toArray(new Post[aux.getSize()]);
                    for (Post p : auxArr) {
                        posts.add(p);
                    }
                }
                if (n instanceof LeafNode) {
                    Post[] childPosts = ((LeafNode) n).getPosts();
                    for (int j = 0; j < n.length; j++) {
                        Post p = childPosts[j];
                        if (postInTheRegion(start, end, p.getLocation())) {
                            posts.add(p);
                        }
                    }
                }
            }
        } else if (nextNode instanceof LeafNode) {
            Post[] childPosts = ((LeafNode) nextNode).getPosts();
            for (int i = 0; i < nextNode.length; i++) {
                Post p = childPosts[i];
                if (postInTheRegion(start, end, p.getLocation())) {
                    posts.add(p);
                }
            }
        }


        return posts;
    }

    //Mira si hi ha interseccio entre dues regions:
    private boolean regionIntersectsRegion(double[] start, double[] end, double[] startN, double[] endN) {
        boolean b1 = postInTheRegion(start, end, startN) || postInTheRegion(start, end, endN);
        boolean b2 = postInTheRegion(startN, endN, start) || postInTheRegion(startN, endN, end);
        return b1 || b2;
    }

    public void removePost(Post post) {
        removePost(post, root);
    }

    public void removePost(double[] location) {
        removePost(location, root);
    }

    //Remove post by reference:
    public void removePost(Post post, Node nextNode) {
        if (nextNode instanceof InternalNode) {
            Node[] child = ((InternalNode) nextNode).getChild();
            for (Node n : child) {
                if (n instanceof LeafNode) {
                    ((LeafNode) n).removePost(post);
                    ((LeafNode) n).findNewLimits();
                } else if (n instanceof InternalNode) {
                    //Mira si els punts estan dins la regio:
                    if (postInTheRegion(n.getStart(), n.getEnd(), post.getLocation())) {
                        removePost(post, n);
                    }
                }
            }
        } else {
            ((LeafNode) nextNode).removePost(post);
            ((LeafNode) nextNode).findNewLimits();
        }
    }

    //Remove post by location:
    public void removePost(double[] postLocation, Node nextNode) {
        if (nextNode instanceof InternalNode) {
            Node[] child = ((InternalNode) nextNode).getChild();
            for (Node n : child) {
                if (n instanceof LeafNode) {
                    ((LeafNode) n).removePost(postLocation);
                } else if (n instanceof InternalNode) {
                    //Mira si els punts estan dins la regio:
                    if (postInTheRegion(n.getStart(), n.getEnd(), postLocation)) {
                        removePost(postLocation, n);
                    }
                }
            }
        } else {
            ((LeafNode) nextNode).removePost(postLocation);
            ((LeafNode) nextNode).findNewLimits();
        }
    }

    private static boolean postInTheRegion(double[] start, double[] end, double[] location) {
        //Mirem si la location esta dins de les x:
        if (start[0] <= location[0] && start[1] <= location[1]) {
            //Mirem si la location esta dins de les y:
            return location[0] <= end[0] && location[1] <= end[1];
        }

        return false;
    }
}