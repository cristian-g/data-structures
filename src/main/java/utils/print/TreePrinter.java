package utils.print;

import datastructures.AVLTree.AVLTree;
import datastructures.HashTable.HashTable;
import datastructures.LinkedList.LinkedList;
import datastructures.RTree.InternalNode;
import datastructures.RTree.LeafNode;
import datastructures.RTree.RTree;
import datastructures.Trie.Trie;
import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.model.Node;
import models.Post;
import models.User;
import test.SimpleElementWithIntegerKey;
import utils.IntegerUtilities;
import utils.ObjectFactory;
import utils.StringUtilities;

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

    public String printAVLTree(AVLTree avlTree) throws IOException {
        return this.printAVLTreeImmersion(avlTree.getRoot());
    }

    public String printAVLTreeImmersion(PrintableNode start) throws IOException {

        final String filename = "out/avl_tree_" + TreePrinter.images_count + ".png";

        LinkTarget[] linkTargets = this.printNodeInfo(start);

        this.count++;
        Graph g = graph("tree").directed().with(
                node(this.count + "").with(Label.html("<b>" + start.getPrintName() + "</b>")).link(linkTargets));

        Graphviz.fromGraph(g).height(2000).render(Format.PNG).toFile(new File(filename));
        TreePrinter.images_count++;

        return filename;
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

    public static RTree initRTreeExample1() {
        RTree rTree = new RTree();

        InternalNode internalNode = new InternalNode(null);
        internalNode.fillCornersWithRandomGeographicCoordinates();
        rTree.setRoot(internalNode);

        InternalNode[] nodes = new InternalNode[RTree.MAX_ITEMS];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new InternalNode(rTree);
            nodes[i].fillCornersWithRandomGeographicCoordinates();

            LeafNode[] nodes2 = new LeafNode[RTree.MAX_ITEMS];
            for (int j = 0; j < nodes2.length; j++) {
                LeafNode leafNode = new LeafNode(null);
                leafNode.fillWithPostsWithRandomGeographicCoordinates(0);
                nodes2[j] = leafNode;
            }
            nodes[i].setChild(nodes2);
        }
        internalNode.setChild(nodes);

        return rTree;
    }

    public String printRTree(RTree rTree) throws IOException {

        final String filename = "out/r_tree_" + TreePrinter.images_count + ".png";

        LinkedList<Node> links = new LinkedList<>();

        this.printRTreeImmersion(rTree.getRoot(), null, "", links);

        Node[] nodeArray = links.toArray(new Node[links.getSize()]);

        Graph g = graph("example3").directed()
                .graphAttr().with(RankDir.BOTTOM_TO_TOP)
                .with(
                        nodeArray
                );

        Graphviz.fromGraph(g).width(9000).render(Format.PNG).toFile(new File(filename));

        return filename;
    }

    public void printRTreeImmersion(datastructures.RTree.Node node, Node parent, String tag, LinkedList<Node> links) {

        LinkedList<java.lang.String> records = new LinkedList<>();
        java.lang.String[] tags = null;

        // Prepare boxes of current node
        if (node instanceof InternalNode) {
            InternalNode internalNode = (InternalNode) node;

            datastructures.RTree.Node[] childs = internalNode.getChild();
            tags = new java.lang.String[childs.length];

            int i = 0;
            for (datastructures.RTree.Node child: childs) {
                tags[i] = "tag" + (this.count);
                if (child == null) continue;
                records.add(rec("tag" + (this.count++), child.computeLabelOfGeographicCoordinates()));
                i++;
            }
        }
        else if (node instanceof LeafNode) {
            LeafNode leafNode = (LeafNode) node;

            Post[] posts = leafNode.getPosts();

            for (Post post: posts) {
                if (post == null) continue;
                if (!post.isVisible()) continue;
                records.add(rec("tag" + (this.count++), post.computeLabel()));
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

            Node node3 = node2.link(between(nodeRoot.port(NORTH).port(), nodeRoot.port(SOUTH)));
            links.add(node3);
        }
        else {

            java.lang.String[] records2 = records.toArray(new java.lang.String[records.getSize()]);

            node2 = node("node" + (this.count++)).with(Records.of(records2));

            Node node3 = node2.link(between(node2.port(NORTH).port(), parent.port(tag, SOUTH)));
            links.add(node3);
        }

        if (node instanceof InternalNode) {

            InternalNode internalNode = (InternalNode) node;
            datastructures.RTree.Node[] childs = internalNode.getChild();

            int i = 0;
            for (datastructures.RTree.Node child: childs) {
                this.printRTreeImmersion(child, node2, tags[i], links);
                i++;
            }
        }
    }

    public static Trie initTrieExample1() {
        Trie trie = new Trie();

        LinkedList<datastructures.Trie.Node> nodeLinkedList1 = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            datastructures.Trie.Node node1 = initRandomNode();
            node1.setWord(StringUtilities.computeRandomString(IntegerUtilities.computeRandomIntegerBetween(6, 9)));
            nodeLinkedList1.add(node1);

            LinkedList<datastructures.Trie.Node> nodeLinkedList2 = new LinkedList<>();
            for (int j = 0; j < 4; j++) {
                datastructures.Trie.Node node2 = initRandomNode();
                node2.setWord(StringUtilities.computeRandomString(IntegerUtilities.computeRandomIntegerBetween(6, 9)));
                nodeLinkedList2.add(node2);

                LinkedList<datastructures.Trie.Node> nodeLinkedList3 = new LinkedList<>();
                for (int k = 0; k < 3; k++) {
                    datastructures.Trie.Node node3 = initRandomNode();
                    node3.setWord(StringUtilities.computeRandomString(IntegerUtilities.computeRandomIntegerBetween(6, 9)));
                    nodeLinkedList3.add(node3);
                }
                node2.setChilds(nodeLinkedList3);
            }
            node1.setChilds(nodeLinkedList2);
        }
        trie.setNodes(nodeLinkedList1);

        return trie;
    }

    public static Trie initTrieExample2() {
        Trie trie = new Trie();

        User[] users = ObjectFactory.computeUsersWithRandomUsername(20);
        for (User user: users) {
            trie.addUser(user);
        }

        return trie;
    }

    private static datastructures.Trie.Node initRandomNode() {
        if (IntegerUtilities.computeRandomIntegerBetween(1, 2) == 1) {
            return new datastructures.Trie.Node();
        }
        return new datastructures.Trie.WordNode(IntegerUtilities.computeRandomIntegerBetween(1, 30));
    }

    public String printTrie(Trie trie) throws IOException {

        final String filename = "out/trie_" + TreePrinter.images_count + ".png";

        // Create root node
        datastructures.Trie.Node start = new datastructures.Trie.Node();
        start.setWord("");
        start.setChilds(trie.getNodes());
        LinkTarget[] linkTargets = this.printTrieImmersion(start);

        this.count++;
        Graph g = graph("tree").directed().with(
                node(this.count + "").with(Label.html("<b>" + start.getPrintName() + "</b>")).link(linkTargets));

        Graphviz.fromGraph(g).height(2000).render(Format.PNG).toFile(new File(filename));
        TreePrinter.images_count++;

        return filename;
    }

    private LinkTarget[] printTrieImmersion(PrintableNode start) {
        if (TreePrinter.max_recursive <= 0) return new LinkTarget[] {};

        TreePrinter.max_recursive--;

        LinkTarget[] linkTargets = new LinkTarget[start.getConnections().length];

        for (int i = 0; i < start.getConnections().length; i++) {

            PrintableNode connection = start.getConnections()[i];

            if (connection == null) {
                this.count++;
                linkTargets[i] = to(node("null: " + this.count).with(Label.html("."), Color.WHITE));
                continue;
            }
            else {
                LinkTarget[] linkTargets1 = this.printNodeInfo(connection);
                this.count++;
                linkTargets[i] = to(node(this.count + "").with(Label.html(connection.getPrintName()), Color.BLACK).link(linkTargets1)).with(Label.html(""), Color.BLACK);
                continue;
            }
        }

        return linkTargets;
    }

    public static HashTable initHashTableExample1() {
        HashTable hashTable = new HashTable();

        int size = hashTable.getArray().length;
        for (int i = 0; i < size; i++) {
            int size2 = IntegerUtilities.computeRandomIntegerBetween(3, 10);
            for (int j = 0; j < size2; j++) {
                SimpleElementWithIntegerKey element = new SimpleElementWithIntegerKey(IntegerUtilities.computeRandomIntegerBetween(0, 10000));
                hashTable.addOn(i, element);
            }
        }

        return hashTable;
    }

    public String printHashTable(HashTable hashTable) throws IOException {

        final String filename = "out/hash_table_" + TreePrinter.images_count + ".png";

        LinkedList<Node> links = new LinkedList<>();

        // Print array
        int size = hashTable.getArray().length;
        java.lang.String[] recordsRoot = new java.lang.String[hashTable.getArray().length];
        for (int i = 0; i < size; i++) {
            recordsRoot[i] = rec("tag" + i, "\n\n\n");
        }

        Node nodeRoot = node(Integer.toString(this.count++)).with(Records.of(recordsRoot));

        int size2 = hashTable.getArray().length;
        for (int i = 0; i < size2; i++) {
            datastructures.LinkedList.Node first = hashTable.getArray()[i].getFirst();
            if (first != null)
                this.printHashTableImmersion(first, nodeRoot, "tag" + i, links);
        }

        Node[] nodeArray = links.toArray(new Node[links.getSize()]);

        Graph g = graph("hash_table").directed()
                .graphAttr().with(RankDir.RIGHT_TO_LEFT)
                .with(
                        nodeArray
                );

        Graphviz.fromGraph(g).width(9000).render(Format.PNG).toFile(new File(filename));

        return filename;
    }

    public void printHashTableImmersion(datastructures.LinkedList.Node<SimpleElementWithIntegerKey> node, Node parent, String tag, LinkedList<Node> links) {
        java.lang.String[] records = new java.lang.String[]{
                rec(node.getPrintName()),
        };
        Node node2 = node("node" + (this.count++)).with(Records.of(records));

        Node node3 = node2.link(between(node2.port(WEST).port(), parent.port(tag, EAST)).with(Arrow.NORMAL.dir(Arrow.DirType.BACK)));
        links.add(node3);

        if (node.getNext() != null) {
            this.printHashTableImmersion(node.getNext(), node2, "", links);
        }
    }
}