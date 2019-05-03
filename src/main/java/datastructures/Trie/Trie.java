package datastructures.Trie;

import datastructures.LinkedList.LinkedList;

public class Trie {

    private int words;  //Max number of suggestions.
    private int users;  //Number of usernames stored in the structure.
    private Node root;

    public Trie() {
        words = 20;
        users = 0;
        root = new Node();
    }

    public void addUser(String username) {

    }

    public void deleteUser(Node actualNode, String username) {
        Node[] children = actualNode.getChilds();
        for(Node n: children) {
            //Si el node actual és el node que conté el username:
            if(n.getKey().equals(username)) {
                if(n.getChilds() == null) {
                    //Eliminar referència al Node.
                    //Si la resta no tenen fills s'ha d'anar eliminant recursivament cap a dalt??
                } else {
                    //Eliminar paraula però no Node.
                }
            } else {
                //Mirar si algun fill te com a seguent lletra, la seguent lletra del username
                //En cas de que la tingui, l'agafo com a actualNode.
                //En cas que no la tingui, miro el seguent.
                //Si cap fill la te, significa que el user no existeix.
            }
        }
    }

    public void addAllUsers(LinkedList<String> usernames) {

    }

    public String[] getSuggestions(String partialName) {
        return null;
    }

    public void limitMemory(int words) {
        this.words = words;
    }
}
