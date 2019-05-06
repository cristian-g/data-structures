package datastructures.Trie;

import utils.print.PrintableNode;

public class WordNode extends Node implements PrintableNode {
    private int searches;

    public WordNode(String key) {
        super(key);
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
