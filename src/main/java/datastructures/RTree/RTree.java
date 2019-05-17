package datastructures.RTree;

import datastructures.ElementWithCoordinates;
import models.Post;
import utils.print.TreePrinter;

import java.util.Arrays;

public class RTree {
    // TODO: Change "MAX_ITEMS" to a more meaningful name
    public static final int MAX_ITEMS = 3;
    public static final int MIN_ITEMS = MAX_ITEMS /2 + MAX_ITEMS %2;

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

    public static RTree getTestRTree() {
        RTree rTree = new RTree();
        rTree.root = new InternalNode(null);
        rTree.root.length = 2;
        LeafNode l1 = new LeafNode(rTree.root);
        ((InternalNode) rTree.root).child[0] = l1;
        LeafNode l2 = new LeafNode(rTree.root);
        ((InternalNode) rTree.root).child[1] = l2;
        l1.addPost(new Post(1, new double[] {0,0}));
        l1.addPost(new Post(2, new double[] {5,5}));
        l2.addPost(new Post(3, new double[] {6,6}));
        l2.addPost(new Post(4, new double[] {7,7}));

        rTree.root.start[0] = 0;
        rTree.root.start[1] = 0;
        rTree.root.end[0] = 7;
        rTree.root.end[1] = 7;

        l1.addPost(new Post(5, new double[]{0,5}));
        l1.addPost(new Post(6, new double[]{5,0}));
        l1.addPost(new Post(7, new double[]{5,0}));
        return rTree;
    }

    public void findCandidates(double[] postLocation, Node root, LinkedList<Node> linkedList) {
        if (postInTheRegion(root.getStart(), root.getEnd(), postLocation)) {
            if (root instanceof LeafNode) {
                linkedList.add(root);
            } else {
                for (Node n : ((InternalNode) root).getChild()) {
                    findCandidates(postLocation, n, linkedList);
                }
            }
        }
    }

    public void addPost(Post post, Node nextNode) {
        double[] postLocation = post.getLocation();

        LinkedList<Node> candidates = new LinkedList<>();
        findCandidates(postLocation, nextNode, candidates);

        /*
        for (Node n : candidates) {

        }
        */

        while (!(nextNode instanceof LeafNode)) {
            Node[] child = ((InternalNode) nextNode).getChild();

            for (Node n : child) {
                if (postInTheRegion(n.getStart(), n.getEnd(), postLocation)) {
                    if (n instanceof LeafNode) {
                        if (n.isFull()) {
                            // TODO: fer el split o comprovar si hi ha alguna altre regio en la que hi cap
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

    public Post getPost(double[] location, Node nextNode) {
        if (postInTheRegion(nextNode.getStart(), nextNode.getEnd(), location)) {
            if (nextNode instanceof InternalNode) {
                Node[] child = ((InternalNode) nextNode).getChild();

                for (Node n : child) {
                    Post tmp = getPost(location, n);

                    if (tmp == null) {
                        continue;
                    }

                    return tmp;
                }
            } else if (nextNode instanceof LeafNode){
                Post[] posts = ((LeafNode) nextNode).getPosts();

                for (Post p : posts) {
                    if (Arrays.equals(p.getLocation(), location)) {
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

    // Returns the posts inside that region
    public Post[] getPosts(double[] start, double[] end) {

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
        //Mirem si la location esta dins de les x:
        if(start[0] <= location[0] && start[1] <= location[1]) {
            //Mirem si la location esta dins de les y:
            if(location[0] <= end[0] && location[1] <= end[1]) {
                return true;
            }
        }

        return false;
    }

    public Node[] split(LeafNode n, Post p) {
        Post[] postsArr = new Post[n.getPosts().length + 1];
        for(int i = 0; i < n.getPosts().length; i++) {
            postsArr[i] = n.getPosts()[i];
        }
        postsArr[n.getPosts().length] = p;

        //Troba els 2 posts mes allunyats entre ells:
        Post[] posts = findFurthestPosts(postsArr);

        //Ara ja tenim els 2 posts mes allunyats entre ells.
        //Creem 2 regions i 2 leafNodes i inserim un post a cada leafNode:
        InternalNode region1 = new InternalNode(n.getParent());
        InternalNode region2 = new InternalNode(n.getParent());
        LeafNode leaf1 = new LeafNode(region1);
        LeafNode leaf2 = new LeafNode(region2);
        leaf1.addPost(posts[0]);
        leaf2.addPost(posts[1]);

        //Ara que ja tenim els 2 posts en les seves regions, s'han d'afegir la resta de posts entre la R1 i la R2:
        addRemainingPosts(leaf1, leaf2, postsArr, posts);
        Node[] res = new Node[4];
        res[0] = region1;
        res[1] = region2;
        res[2] = leaf1;
        res[3] = leaf2;
        return res;
    }

    public void addRemainingPosts(LeafNode leaf1, LeafNode leaf2, Post[] postsArr, Post[] posts) {
        int postsToAdd = postsArr.length - 2;
        for(Post p: postsArr) {
            if(p != posts[0] && p != posts[1]) {
                if(MIN_ITEMS - leaf1.getLength() == postsToAdd) {
                    leaf1.addPost(p);
                } else if(MIN_ITEMS - leaf2.getLength() == postsToAdd) {
                    leaf2.addPost(p);
                } else {
                    if(findNearestRegion(leaf1, leaf2, p)) {
                        leaf1.addPost(p);
                    } else {
                        leaf2.addPost(p);
                    }
                }
                postsToAdd--;
            }
        }
    }

    //Mira a quina de les dues regions hi hauria increment d'area mes petit per a inserir el post en aquella regio:
    public boolean findNearestRegion(LeafNode leaf1, LeafNode leaf2, Post p) {
        double inc1 = calculaIncrement(leaf1.getStart(), leaf1.getEnd(), p.getLocation());
        double inc2 = calculaIncrement(leaf2.getStart(), leaf2.getEnd(), p.getLocation());
        return inc1 > inc2;
    }


    //Retorna els dos posts mes allunyats entre ells:
    public Post[] findFurthestPosts(Post[] postsArr) {
        double max = 0;
        Post[] posts = new Post[2];
        for(int i = 0; i < postsArr.length; i++) {
            for(int j = i + 1; j < postsArr.length; j++) {
                double dist = calculateDistance(postsArr[i], postsArr[j]);
                if(dist > max) {
                    max = dist;
                    posts[0] = postsArr[i];
                    posts[1] = postsArr[j];
                }
            }
        }
        return posts;
    }

    //Retorna la distancia al quadrat que hi ha entre post1 i post2:
    public double calculateDistance(Post post1, Post post2) {
        double[] locationP1 = post1.getLocation();
        double[] locationP2 = post2.getLocation();
        return (Math.pow(locationP1[0] - locationP2[0], 2) + Math.pow(locationP1[1] - locationP2[1], 2));
    }

}