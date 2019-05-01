package utils.print;

import com.sun.org.apache.xpath.internal.operations.String;
import datastructures.LinkedList.LinkedList;
import datastructures.RTree.InternalNode;
import datastructures.RTree.LeafNode;
import datastructures.RTree.RTree;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.model.Node;
import models.Post;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.attribute.Records.rec;
import static guru.nidi.graphviz.model.Compass.*;
import static guru.nidi.graphviz.model.Factory.*;
import static guru.nidi.graphviz.model.Link.between;

public class TreePrinter {
    public static int max_recursive = Integer.MAX_VALUE;// Limit can be "disabled" using Integer.MAX_VALUE
    private int count;
    private static int images_count = 0;

    public TreePrinter() {
        this.count = 0;
    }

    public void printGraph(PrintableNode start) {
        LinkTarget[] linkTargets = this.printNodeInfo(start);

        this.count++;
        Graph g = graph("tree").directed().with(
                node(this.count + "").with(Label.html("<b>" + start.getPrintName() + "</b>")).link(linkTargets));

        try {
            Graphviz.fromGraph(g).height(2000).render(Format.PNG).toFile(new File("out/" + TreePrinter.images_count + ".png"));
            TreePrinter.images_count++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LinkTarget[] printNodeInfo(PrintableNode start) {
        if (TreePrinter.max_recursive <= 0) return new LinkTarget[] {};

        TreePrinter.max_recursive--;

        LinkTarget[] linkTargets = new LinkTarget[start.getConnections().length];
        for (int i = 0; i < start.getConnections().length; i++) {

            PrintableNode connection = start.getConnections()[i];

            if (connection == null) {
                this.count++;
                linkTargets[i] = to(node("null: " + this.count).with(Label.html("."), Color.WHITE))/*.with(Style.BOLD, Label.html("."), Color.WHITE)*/;
                continue;
            }
            else {
                LinkTarget[] linkTargets1 = this.printNodeInfo(connection);
                this.count++;
                linkTargets[i] = to(node(this.count + "").with(Label.html("<b>" + connection.getPrintName() + "</b>"), Color.BLACK).link(linkTargets1)).with(Style.BOLD, Label.html(""), Color.BLACK);
                continue;
            }
        }

        return linkTargets;
    }

    public void resetStats() {
        max_recursive = Integer.MAX_VALUE;// Limit can be disabled using Integer.MAX_VALUE
        count = 0;
    }

    private RTree initRTreeExample1() {
        RTree rTree = new RTree();

        InternalNode internalNode = new InternalNode(null);
        internalNode.fillCornersWithRandomGeographicCoordinates();
        rTree.setRoot(internalNode);

        InternalNode[] nodes = new InternalNode[RTree.ARRAY_SIZE];
        for (int i = 0; i < nodes.length; i++) {
            LeafNode[] nodes2 = new LeafNode[RTree.ARRAY_SIZE];
            for (int j = 0; j < nodes2.length; j++) {
                LeafNode leafNode = new LeafNode(null);
                leafNode.fillWithPostsWithRandomGeographicCoordinates(0);
                nodes2[j] = leafNode;
            }
            nodes[i] = new InternalNode(internalNode);
            nodes[i].fillCornersWithRandomGeographicCoordinates();

            nodes[i].setChild(nodes2);
        }
        internalNode.setChild(nodes);

        return rTree;
    }

    public void printRTree() {

        RTree rTree = initRTreeExample1();

        LinkedList<Node> nodes = new LinkedList<>();
        LinkedList<Node> links = new LinkedList<>();


        this.printRTreeImmersion(rTree.getRoot(), null, nodes, links);




        /*Node[] nodeArray = new Node[]{
                node0.link(
                        between(port("f0"), node1.port("v", SOUTH)),
                        between(port("f1"), node2.port(WEST)),
                        between(port("f2"), node3.port(WEST)),
                        between(port("f3"), node4.port(WEST)),
                        between(port("f4"), node5.port("v", NORTH))),
                node2.link(between(port("p"), node6.port(NORTH_WEST))),
                node4.link(between(node4.port(SOUTH).port(), node7.port(NORTH)))
        };*/
        Node[] nodeArray = links.toArray(new Node[links.getSize()]);

        Graph g = graph("example3").directed()
                .graphAttr().with(RankDir.BOTTOM_TO_TOP)
                .with(
                        nodeArray
                );



        try {
            Graphviz.fromGraph(g).width(9000).render(Format.PNG).toFile(new File("out/r_tree_example.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printRTreeImmersion(datastructures.RTree.Node node, Node parent, LinkedList<Node> nodes, LinkedList<Node> links) {

        LinkedList<java.lang.String> records = new LinkedList<>();

        // Prepare boxes of current node
        if (node instanceof InternalNode) {
            InternalNode internalNode = (InternalNode) node;

            datastructures.RTree.Node[] childs = ((InternalNode) node).getChild();

            for (datastructures.RTree.Node child: childs) {
                records.insert(rec("tag" + (this.count++), child.computeLabelOfGeographicCoordinates()));
            }
        }
        else if (node instanceof LeafNode) {
            LeafNode leafNode = (LeafNode) node;

            Post[] posts = leafNode.getPosts();

            for (Post post: posts) {
                records.insert(rec("tag" + (this.count++), post.computeLabel()));
            }
        }

        // Create current node
        Node node2 = null;

        if (parent == null) {

            // This is the root node, so we must create the main region
            java.lang.String[] recordsRoot = new java.lang.String[]{
                    rec(node.computeLabelOfGeographicCoordinates()),
            };

            Node nodeRoot = node(Integer.toString(this.count++)).with(Records.of(recordsRoot));

            java.lang.String[] records2 = records.toArray(new java.lang.String[records.getSize()]);

            node2 = node("node" + (this.count++)).with(Records.of(records2));

            Node node3 = nodeRoot.link(between(nodeRoot.port(NORTH).port(), node2.port(SOUTH)));
            links.insert(node3);
        }
        else {

            java.lang.String[] records2 = records.toArray(new java.lang.String[records.getSize()]);

            node2 = node("node" + (this.count++)).with(Records.of(records2));

            Node node3 = parent.link(between(parent.port(NORTH).port(), node2.port(SOUTH)));
            links.insert(node3);

            Node node4 = node2.link(between(node2.port(NORTH).port(), node3.port(SOUTH)));
            links.insert(node4);
        }




        if (node instanceof InternalNode) {

            InternalNode internalNode = (InternalNode) node;
            datastructures.RTree.Node[] childs = ((InternalNode) node).getChild();

            for (datastructures.RTree.Node child: childs) {
                this.printRTreeImmersion(child, node2, nodes, links);
            }
        }
        else if (node instanceof LeafNode) {

            LeafNode leafNode = (LeafNode) node;
            Post[] posts = leafNode.getPosts();

            /*for (Post post: posts) {
                this.printRTreeImmersion(post, node2, nodes, links);
            }*/
        }



    }

    public void printRTreeExample() {

        java.lang.String[] records = new java.lang.String[]{
                rec("f0", ""),
                rec("f1", ""),
                rec("f2", ""),
                rec("f3", ""),
                rec("f4", "")
        };

        Node
                node0 = node("node0").with(Records.of(records)),
                node1 = node("node1").with(Records.of(rec("n4"), rec("v", "719"), rec(""))),
                node2 = node("node2").with(Records.of(rec("a1"), rec("805"), rec("p", ""))),
                node3 = node("node3").with(Records.of(rec("i9"), rec("718"), rec(""))),
                node4 = node("node4").with(Records.of(rec("e5"), rec("989"), rec("p", ""))),
                node5 = node("node5").with(Records.of(rec("t2"), rec("v", "959"), rec(""))),
                node6 = node("node6").with(Records.of(rec("o1"), rec("794"), rec(""))),
                node7 = node("node7").with(Records.of(rec("s7"), rec("659"), rec(""), rec("tag294", "show_this<br>wsds\ncoord 43")));

        Graph g = graph("example3").directed()
                .graphAttr().with(RankDir.TOP_TO_BOTTOM)
                .with(
                        new Node[]{node0.link(
                                between(port("f0"), node1.port("v", SOUTH)),
                                between(port("f1"), node2.port(WEST)),
                                between(port("f2"), node3.port(WEST)),
                                between(port("f3"), node4.port(WEST)),
                                between(port("f4"), node5.port("v", NORTH))),
                                node2.link(between(port("p"), node6.port(NORTH_WEST))),
                                node4.link(between(node4.port(SOUTH).port(), node7.port(NORTH)))}
                );

        try {
            Graphviz.fromGraph(g).width(900).render(Format.PNG).toFile(new File("out/r_tree_example.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}