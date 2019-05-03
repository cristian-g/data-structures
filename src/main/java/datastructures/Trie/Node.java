package datastructures.Trie;

import datastructures.LinkedList.LinkedList;

public class Node {
    private String key;
    private LinkedList<Node> childs;

    public Node() {

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
}
