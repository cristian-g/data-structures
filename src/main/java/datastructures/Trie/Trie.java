package datastructures.Trie;

import datastructures.LinkedList.LinkedList;
import models.User;

import java.util.Arrays;

public class Trie {

    public static String DATA_STRUCTURE_NAME = "Trie";

    private int limit;  //Max number of suggestions.
    private int users;  //Number of usernames stored in the structure.
    private LinkedList<Node> nodes;

    public Trie() {
        //this.limit = Integer.MAX_VALUE;// Uncomment this line to "disable" the limit
        this.limit = 20;
        this.users = 0;
        this.nodes = new LinkedList<>();
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
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

    public void addUser(User u, int searches) {
        this.users++;
        String username = u.getUsername().toLowerCase();
        char[] charArray = username.toCharArray();
        String auxKey = "";
        LinkedList<Node> actualNodes = nodes;
        //Per totes les lletres de la paraula:
        for(int i = 0; i < charArray.length; i++) {
            auxKey += charArray[i];
            //Si hi ha la lletra en els nodes actuals:
            if(actualNodes.contains(charArray[i])) {
                if(i == charArray.length - 1) {
                    WordNode newNode = new WordNode(actualNodes.getByIntegerKey(charArray[i]), u, searches);
                    actualNodes.removeByIntegerKey(charArray[i]);
                    actualNodes.add(newNode);
                } else {
                    String key = actualNodes.getByIntegerKey(charArray[i]).getWord();
                    char[] charkey = key.toCharArray();
                    //Si la lletra coincideix, seguir aquell camí:
                    if(charArray[i] == charkey[i]) {
                        actualNodes = actualNodes.getByIntegerKey(charArray[i]).getChilds();
                    }
                }
            //Si no hi ha la lletra en els nodes actuals:
            } else {
                //Si es la ultima lletra de la paraula, inserir word:
                if(i == charArray.length - 1) {
                    WordNode newNode = new WordNode(auxKey, u, searches);
                    actualNodes.add(newNode);
                //Si no es la ultima lletra de la paraula, inserir cami:
                } else {
                    Node newNode = new Node(auxKey);
                    actualNodes.add(newNode);
                    actualNodes = actualNodes.getByIntegerKey(charArray[i]).getChilds();
                }
            }
        }
    }

    public void addUser(User u) {
        this.addUser(u, 0);
    }

    public void deleteUser(String username) {
        this.users--;
        username = username.toLowerCase();
        char[] charArray = username.toCharArray();
        LinkedList<Node> actualNodes = nodes;
        Node[] path = new Node[username.length()];

        for(int i = 0; i < username.length() ; i++) {
            Node nodeActual = actualNodes.getByIntegerKey(charArray[i]);
            path[i] = nodeActual;
            if(i < username.length() - 1) {
                actualNodes = nodeActual.getChilds();
            }
        }
        if(actualNodes.getByIntegerKey(charArray[username.length() - 1]).getChilds().getSize() == 0) {
            //Si no te fills, eliminar Node:
            actualNodes.removeByIntegerKey(charArray[username.length() - 1]);
        } else {
            //Si te fills, convertir WordNode a Node:
            Node newNode = new Node(actualNodes.getByIntegerKey(charArray[username.length() - 1]).getWord(), actualNodes.getByIntegerKey(charArray[username.length() - 1]).getChilds());
            actualNodes.removeByIntegerKey(charArray[username.length() - 1]);
            actualNodes.add(newNode);
        }
        //Cami ascendent borrant referencies:
        for(int i = username.length() - 2; i > -1; i--) {
            actualNodes = nodes;
            for(int j = 0; j < i; j++) {
                actualNodes = actualNodes.getByIntegerKey(charArray[j]).getChilds();
            }
            if(actualNodes.getByIntegerKey(charArray[i]) instanceof WordNode || actualNodes.getByIntegerKey(charArray[i]).getChilds().getSize() > 0) {
                break;
            }
            actualNodes.removeByIntegerKey(charArray[i]);
        }
    }

    public void addAllUsers(LinkedList<User> usernames) {
        User[] usernamesArr = usernames.toArray(new User[usernames.getSize()]);
        for(User u: usernamesArr) {
            if(users == limit) {
                System.out.println("Se han introducido los " + limit + " primeros usuarios!");
                break;
            }
            addUser(u);
        }
    }

    public LinkedList<User> getSuggestions(String partialName) {
        partialName = partialName.toLowerCase();
        char[] charArray = partialName.toCharArray();
        String auxKey = "";
        LinkedList<Node> actualNodes = nodes;
        LinkedList<User> suggestions = new LinkedList<>();
        //Per totes les lletres de la paraula:
        for(int i = 0; i < charArray.length; i++) {
            auxKey += charArray[i];
            //Si hi ha la lletra en els nodes actuals:
            if(actualNodes.contains(charArray[i])) {
                String key = actualNodes.getByIntegerKey(charArray[i]).getWord();
                char[] charkey = key.toCharArray();
                //Si la lletra coincideix, seguir aquell camí:
                if(charArray[i] == charkey[i]) {
                    if(i == charArray.length - 1 && actualNodes.getByIntegerKey(charArray[i]) instanceof WordNode) {
                        suggestions.add(((WordNode) actualNodes.getByIntegerKey(charArray[i])).getUser());
                        ((WordNode) actualNodes.getByIntegerKey(charArray[i])).incrementSearches();
                    }
                    actualNodes = actualNodes.getByIntegerKey(charArray[i]).getChilds();
                }
            } else {
                //No hi ha cap suggerència, retorna una llista buida:
                return new LinkedList<>();
            }
        }
        Node[] nodesActuals = actualNodes.toArray(new Node[actualNodes.getSize()]);
        for(Node n: nodesActuals) {
            LinkedList<User> aux = searchForWord(n, new LinkedList<>());
            User[] auxArray = aux.toArray(new User[aux.getSize()]);
            for(User user: auxArray) {
                suggestions.add(user);
            }
        }
        return suggestions;
    }

    private LinkedList<Word> getWords(String partialName) {
        partialName = partialName.toLowerCase();
        char[] charArray = partialName.toCharArray();
        String auxKey = "";
        LinkedList<Node> actualNodes = nodes;
        LinkedList<Word> suggestions = new LinkedList<>();
        //Per totes les lletres de la paraula:
        for(int i = 0; i < charArray.length; i++) {
            auxKey += charArray[i];
            //Si hi ha la lletra en els nodes actuals:
            if(actualNodes.contains(charArray[i])) {
                String key = actualNodes.getByIntegerKey(charArray[i]).getWord();
                char[] charkey = key.toCharArray();
                //Si la lletra coincideix, seguir aquell camí:
                if(charArray[i] == charkey[i]) {
                    if(i == charArray.length - 1 && actualNodes.getByIntegerKey(charArray[i]) instanceof WordNode) {
                        suggestions.add(new Word(actualNodes.getByIntegerKey(charArray[i]).getWord(), ((WordNode) actualNodes.getByIntegerKey(charArray[i])).getSearches()));
                    }
                    actualNodes = actualNodes.getByIntegerKey(charArray[i]).getChilds();
                }
            } else {
                //No hi ha cap suggerència, retorna una llista buida:
                return new LinkedList<>();
            }
        }
        Node[] nodesActuals = actualNodes.toArray(new Node[actualNodes.getSize()]);
        for(Node n: nodesActuals) {
            LinkedList<Word> aux = searchForWordAux(n, new LinkedList<>());
            Word[] auxArray = aux.toArray(new Word[aux.getSize()]);
            for(Word w: auxArray) {
                suggestions.add(w);
            }
        }
        return suggestions;
    }

    private LinkedList<Word> searchForWordAux(Node nodeActual, LinkedList<Word> suggestions) {
        if(nodeActual instanceof WordNode) {
            suggestions.add(new Word(nodeActual.getWord(), ((WordNode) nodeActual).getSearches()));
        }
        Node[] actualNodes = nodeActual.getChilds().toArray(new Node[nodeActual.getChilds().getSize()]);
        for(Node n: actualNodes) {
            searchForWordAux(n, suggestions);
        }
        return suggestions;
    }

    private LinkedList<User> searchForWord(Node nodeActual, LinkedList<User> suggestions) {
        if(nodeActual instanceof WordNode) {
            suggestions.add(((WordNode) nodeActual).getUser());
            ((WordNode) nodeActual).incrementSearches();
        }
        Node[] actualNodes = nodeActual.getChilds().toArray(new Node[nodeActual.getChilds().getSize()]);
        for(Node n: actualNodes) {
            searchForWord(n, suggestions);
        }
        return suggestions;
    }

    public void limitMemory(int limit) {
        this.limit = limit;

        if(limit < users) {
            //Agafa tots els noms guardats per ordre de cerques ascendent:
            LinkedList<Word> wordList = getWords("");
            Word[] usernames = wordList.toArray(new Word[wordList.getSize()]);
            Arrays.sort(usernames);

            //Elimina els noms guardats amb menys cerques fins que quedin 'limit' noms:
            int i = 0;
            while(limit < users) {
                deleteUser(usernames[i++].getWord());
            }
        }
    }
}
