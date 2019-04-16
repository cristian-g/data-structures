package utils.print;

import guru.nidi.graphviz.attribute.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.LinkTarget;

import java.io.File;
import java.io.IOException;

import static guru.nidi.graphviz.model.Factory.*;

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
}