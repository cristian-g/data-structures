package datastructures.Trie;

import datastructures.ElementWithIntegerKey;
import datastructures.LinkedList.LinkedList;
import utils.print.PrintableNode;

public class Node implements PrintableNode, ElementWithIntegerKey {
    private String word;
    private LinkedList<Node> childs;

    public Node() {
        this.childs = new LinkedList<>();
    }

    public Node(String word) {
        this.word = word;
        this.childs = new LinkedList<>();
    }

    public Node(WordNode node) {
        this.word = node.getWord();
        this.childs = node.getChilds();
    }

    /*
        DUBTE:
            Per eliminar una paraula que té fills, passar de WordNode a Node.
            Però quan volem eliminar una paraula, haurem de canviar el node de tipus WordNode a tipus Node.
     */


    public int getKey() {
        return word.charAt(word.length() - 1);
    }

    public String getWord() {
        return word;
    }

    public LinkedList<Node> getChilds() {
        return childs;
    }

    public void setWord(String word) {
        this.word = word;
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
        String string = this.word;
        if (string.length() > 0) {
            return string.substring(string.length() - 1);
        }
        else {
            return "Trie";
        }
    }
}
