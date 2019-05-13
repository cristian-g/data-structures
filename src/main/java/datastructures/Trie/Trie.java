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
            if(actualNodes.contains(charArray[i])) {
                if(i == charArray.length - 1) {
                    WordNode newNode = new WordNode(actualNodes.getByKey(charArray[i]));
                    actualNodes.removeByKey(charArray[i]);
                    actualNodes.insert(newNode);
                } else {
                    String key = actualNodes.getByKey(charArray[i]).getWord();
                    char[] charkey = key.toCharArray();
                    //Si la lletra coincideix, seguir aquell camí:
                    if(charArray[i] == charkey[i]) {
                        actualNodes = actualNodes.getByKey(charArray[i]).getChilds();
                    }
                }
            //Si no hi ha la lletra en els nodes actuals:
            } else {
                //Si es la ultima lletra de la paraula, inserir word:
                if(i == charArray.length - 1) {
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

    public void deleteUser(String username) {
        username = username.toLowerCase();
        char[] charArray = username.toCharArray();
        LinkedList<Node> actualNodes = nodes;
        Node[] path = new Node[username.length()];

        for(int i = 0; i < username.length() ; i++) {
            Node nodeActual = actualNodes.getByKey(charArray[i]);
            System.out.println(nodeActual.getWord());
            path[i] = nodeActual;
            if(i < username.length() - 1) {
                actualNodes = nodeActual.getChilds();
            }
        }
        if(actualNodes.getByKey(charArray[username.length() - 1]).getChilds().getSize() == 0) {
            //Si no te fills, eliminar Node:
            actualNodes.removeByKey(charArray[username.length() - 1]);
        } else {
            //Si te fills, convertir WordNode a Node:
            Node newNode = new Node(actualNodes.getByKey(charArray[username.length() - 1]).getWord(), actualNodes.getByKey(charArray[username.length() - 1]).getChilds());
            actualNodes.removeByKey(charArray[username.length() - 1]);
            actualNodes.insert(newNode);
        }
        //Cami ascendent borrant referencies:
        for(int i = username.length() - 2; i > -1; i--) {
            actualNodes = nodes;
            for(int j = 0; j < i; j++) {
                actualNodes = actualNodes.getByKey(charArray[j]).getChilds();
            }
            if(actualNodes.getByKey(charArray[i]).getChilds().getSize() > 0) {
                break;
            }
            actualNodes.removeByKey(charArray[i]);
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
                //No hi ha cap suggerència, retorna una llista buida:
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
