package datastructures.Trie;

public class Node {
    private boolean isWord;
    private String key;
    private Node[] childs;

    public Node() {

    }

    /*
        DUBTE:
            Si és una paraula, serà un WordNode.
            Aleshores no fa falta tenir el boolean de isWord pq podem utilitzar el instanceOf.
            Però quan volem eliminar una paraula, haurem de canviar el node de tipus WordNode a tipus Node.
     */

    public boolean isWord() {
        return isWord;
    }

    public String getKey() {
        return key;
    }

    public Node[] getChilds() {
        return childs;
    }
}
