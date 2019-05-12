package datastructures.Trie;

import utils.print.PrintableNode;

public class WordNode extends Node implements PrintableNode {
    private int searches;

    public WordNode(int searches) {
        super();
        this.searches = searches;
    }

    public WordNode(String word) {
        super(word);
        this.searches = 0;
    }

    public WordNode(Node n) {
        super(n.getWord(), n.getChilds());
        this.searches = 0;
    }

    public int getSearches() {
        return searches;
    }

    public void setSearches(int searches) {
        this.searches = searches;
    }

    @Override
    public String getPrintName() {
        return super.getPrintName() + "<br/><br/><b>" + this.searches + "</b>";
    }
}
