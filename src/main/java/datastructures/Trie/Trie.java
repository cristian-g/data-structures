package datastructures.Trie;

import datastructures.LinkedList.LinkedList;

public class Trie {

    private int words;  //Max number of suggestions.
    private int users;  //Number of usernames stored in the structure.
    private LinkedList<Node> nodes;

    public Trie() {
        this.words = 20;
        this.users = 0;
        this.nodes = new LinkedList<>();
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public LinkedList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(LinkedList<Node> nodes) {
        this.nodes = nodes;
    }

    public void addUser(String username) {
        this.users++;
        username = username.toLowerCase();
        char[] charArray = username.toCharArray();
        String auxKey = "";
        LinkedList<Node> actualNodes = nodes;
        //Per totes les lletres de la paraula:
        for(int i = 0; i < charArray.length; i++) {
            auxKey += charArray[i];
            //Si hi ha la lletra en els nodes actuals:
            if (actualNodes.contains(charArray[i])) {
                String key = actualNodes.getByKey(charArray[i]).getWord();
                char[] charkey = key.toCharArray();
                //Si la lletra coincideix, seguir aquell camí:
                if (charArray[i] == charkey[i]) {
                    actualNodes = actualNodes.getByKey(charArray[i]).getChilds();
                }
            //Si no hi ha la lletra en els nodes actuals:
            } else {
                //Si es la ultima lletra de la paraula, inserir word:
                if (i == charArray.length - 1) {
                    WordNode newNode = new WordNode(auxKey);
                    actualNodes.insert(newNode);
                    //Si no es la ultima lletra de la paraula, inserir cami:
                } else {
                    Node newNode = new Node(auxKey);
                    actualNodes.insert(newNode);
                    actualNodes = actualNodes.getByKey(charArray[i]).getChilds();
                }
            }
        }
    }

    public void deleteUser(Node actualNode, String username) {
        Node[] children = actualNode.getChilds().toArray(new Node[actualNode.getChilds().getSize()]);
        for(Node n: children) {
            //Si el node actual és el node que conté el username:
            if(n.getWord().equals(username) && (n instanceof WordNode)) {
                if(n.getChilds() == null) {
                    //Eliminar referència al Node des del pare.
                    //Si el pare no té fills s'ha d'anar eliminant recursivament cap a dalt fins que tingui fills o siguin WordNode??
                } else {
                    Node newNode = new Node((WordNode)n);
                    //Ara necessito que el pare tingui la referència al newNode com a child.
                    //Potser cada node hauria de tenir un punter al pare??
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
        String[] users = usernames.toArray(new String[usernames.getSize()]);
        for(String u: users) {
            addUser(u);
        }
    }

    public LinkedList<String> getSuggestions(String partialName) {
        partialName = partialName.toLowerCase();
        char[] charArray = partialName.toCharArray();
        String auxKey = "";
        LinkedList<Node> actualNodes = nodes;
        //Per totes les lletres de la paraula:
        for(int i = 0; i < charArray.length; i++) {
            auxKey += charArray[i];
            //Si hi ha la lletra en els nodes actuals:
            if (actualNodes.contains(charArray[i])) {
                String key = actualNodes.getByKey(charArray[i]).getWord();
                char[] charkey = key.toCharArray();
                //Si la lletra coincideix, seguir aquell camí:
                if (charArray[i] == charkey[i]) {
                    actualNodes = actualNodes.getByKey(charArray[i]).getChilds();
                }
                //Si no hi ha la lletra en els nodes actuals:
            } else {
                //No hi ha cap suggerència.
                return new LinkedList<>();
            }
        }
        Node[] nodesActuals = actualNodes.toArray(new Node[actualNodes.getSize()]);

        for(Node n: nodesActuals) {
            System.out.println("Hello");
            //Implementar funció recursiva de cerca que recorri totes les branques i retorni les primeres "word" paraules d'aquestes.
        }
        return null;
    }

    public void limitMemory(int words) {
        this.words = words;
    }
}
