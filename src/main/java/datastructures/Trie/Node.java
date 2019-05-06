package datastructures.Trie;

import datastructures.LinkedList.LinkedList;
import utils.print.PrintableNode;

public class Node implements PrintableNode {
    private String key;
    private LinkedList<Node> childs;

    public Node(String key) {
        this.key = key;
        this.childs = new LinkedList<>();
    }

    public Node(WordNode node) {
        this.key = node.getKey();
        this.childs = node.getChilds();
    }

    /*
        DUBTE:
            Per eliminar una paraula que té fills, passar de WordNode a Node.
            Però quan volem eliminar una paraula, haurem de canviar el node de tipus WordNode a tipus Node.
     */


    public String getKey() {
        return key;
    }

    public LinkedList<Node> getChilds() {
        return childs;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setChilds(LinkedList<Node> childs) {
        this.childs = childs;
    }

    public void addChild(Node n) {
        childs.insert(n);
    }

    @Override
    public PrintableNode[] getConnections() {
        PrintableNode[] printableNodes = this.childs.toArray(new PrintableNode[this.childs.getSize()]);
        return printableNodes;
    }

    @Override
    public String getPrintName() {
        String string = this.key;
        if (string.length() > 0) {
            return string.substring(string.length() - 1);
        }
        else {
            return "Trie";
        }
    }
}
