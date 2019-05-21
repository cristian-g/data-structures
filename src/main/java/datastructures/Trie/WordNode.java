package datastructures.Trie;

import models.User;
import utils.print.PrintableNode;

public class WordNode extends Node implements PrintableNode {
    private int searches;
    private User user;

    public WordNode(String word, User user) {
        super(word);
        this.searches = 0;
        this.user = user;
    }

    public WordNode(String word, User user, int searches) {
        super(word);
        this.searches = searches;
        this.user = user;
    }

    public WordNode(Node n, User user) {
        super(n.getWord(), n.getChilds());
        this.searches = 0;
        this.user = user;
    }

    public WordNode(Node n, User user, int searches) {
        super(n.getWord(), n.getChilds());
        this.searches = searches;
        this.user = user;
    }

    public WordNode(int searches) {
        super();
        this.searches = searches;
    }

    public int getSearches() {
        return searches;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void incrementSearches() {
        searches++;
    }

    @Override
    public String getPrintName() {
        return super.getPrintName() + "<br/><br/><b>" + this.searches + "</b>";
    }
}
