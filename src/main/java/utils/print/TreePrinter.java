package utils.print;

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.LinkTarget;
import guru.nidi.graphviz.model.Node;

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

    public void printRTreeExample() {

        Node
                node0 = node("node0").with(Records.of(rec("f0", ""), rec("f1", ""), rec("f2", ""), rec("f3", ""), rec("f4", ""))),
                node1 = node("node1").with(Records.of(rec("n4"), rec("v", "719"), rec(""))),
                node2 = node("node2").with(Records.of(rec("a1"), rec("805"), rec("p", ""))),
                node3 = node("node3").with(Records.of(rec("i9"), rec("718"), rec(""))),
                node4 = node("node4").with(Records.of(rec("e5"), rec("989"), rec("p", ""))),
                node5 = node("node5").with(Records.of(rec("t2"), rec("v", "959"), rec(""))),
                node6 = node("node6").with(Records.of(rec("o1"), rec("794"), rec(""))),
                node7 = node("node7").with(Records.of(rec("s7"), rec("659"), rec("")));
        Graph g = graph("example3").directed()
                .graphAttr().with(RankDir.TOP_TO_BOTTOM)
                .with(
                        node0.link(
                                between(port("f0"), node1.port("v", SOUTH)),
                                between(port("f1"), node2.port(WEST)),
                                between(port("f2"), node3.port(WEST)),
                                between(port("f3"), node4.port(WEST)),
                                between(port("f4"), node5.port("v", NORTH))),
                        node2.link(between(port("p"), node6.port(NORTH_WEST))),
                        node4.link(between(port("p"), node7.port(SOUTH_WEST))));

        try {
            Graphviz.fromGraph(g).width(900).render(Format.PNG).toFile(new File("out/r_tree_example.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}