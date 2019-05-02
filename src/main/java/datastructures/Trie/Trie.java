package datastructures.Trie;

import datastructures.LinkedList.LinkedList;
import models.User;

public class Trie {

    private int words;  //Max number of suggestions.
    private int users;  //Number of usernames stored in the structure.
    private Node root;

    public Trie() {
        words = 20;
        users = 0;
        root = new Node();
    }

    public void addWord() {

    }

    private void addLetter() {

    }

    public void addAllUsers(LinkedList<User> users) {

    }

    public void limitMemory(int words) {
        this.words = words;
    }
}
