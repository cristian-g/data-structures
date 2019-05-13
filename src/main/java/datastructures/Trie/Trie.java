package datastructures.Trie;

import datastructures.LinkedList.LinkedList;

import java.util.Arrays;

public class Trie {

    private int limit;  //Max number of suggestions.
    private int users;  //Number of usernames stored in the structure.
    private LinkedList<Node> nodes;

    public Trie() {
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
        this.users--;
        username = username.toLowerCase();
        char[] charArray = username.toCharArray();
        LinkedList<Node> actualNodes = nodes;
        Node[] path = new Node[username.length()];

        for(int i = 0; i < username.length() ; i++) {
            Node nodeActual = actualNodes.getByKey(charArray[i]);
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
        String[] usernamesArr = usernames.toArray(new String[usernames.getSize()]);
        for(String u: usernamesArr) {
            if(users == limit) {
                System.out.println("Se han introducido los " + limit + " primeros usuarios!");
                break;
            }
            addUser(u);
        }
    }

    public LinkedList<String> getSuggestions(String partialName) {
        partialName = partialName.toLowerCase();
        char[] charArray = partialName.toCharArray();
        String auxKey = "";
        LinkedList<Node> actualNodes = nodes;
        LinkedList<String> suggestions = new LinkedList<>();
        //Per totes les lletres de la paraula:
        for(int i = 0; i < charArray.length; i++) {
            auxKey += charArray[i];
            //Si hi ha la lletra en els nodes actuals:
            if(actualNodes.contains(charArray[i])) {
                String key = actualNodes.getByKey(charArray[i]).getWord();
                char[] charkey = key.toCharArray();
                //Si la lletra coincideix, seguir aquell camí:
                if(charArray[i] == charkey[i]) {
                    if(i == charArray.length - 1 && actualNodes.getByKey(charArray[i]) instanceof WordNode) {
                        suggestions.insert(actualNodes.getByKey(charArray[i]).getWord());
                        ((WordNode) actualNodes.getByKey(charArray[i])).incrementSearches();
                    }
                    actualNodes = actualNodes.getByKey(charArray[i]).getChilds();
                }
            } else {
                //No hi ha cap suggerència, retorna una llista buida:
                return new LinkedList<>();
            }
        }
        Node[] nodesActuals = actualNodes.toArray(new Node[actualNodes.getSize()]);
        for(Node n: nodesActuals) {
            LinkedList<String> aux = searchForWord(n, new LinkedList<>());
            String[] auxArray = aux.toArray(new String[aux.getSize()]);
            for(String s: auxArray) {
                suggestions.insert(s);
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
                String key = actualNodes.getByKey(charArray[i]).getWord();
                char[] charkey = key.toCharArray();
                //Si la lletra coincideix, seguir aquell camí:
                if(charArray[i] == charkey[i]) {
                    if(i == charArray.length - 1 && actualNodes.getByKey(charArray[i]) instanceof WordNode) {
                        suggestions.insert(new Word(actualNodes.getByKey(charArray[i]).getWord(), ((WordNode) actualNodes.getByKey(charArray[i])).getSearches()));
                    }
                    actualNodes = actualNodes.getByKey(charArray[i]).getChilds();
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
                suggestions.insert(w);
            }
        }
        return suggestions;
    }

    private LinkedList<Word> searchForWordAux(Node nodeActual, LinkedList<Word> suggestions) {
        if(nodeActual instanceof WordNode) {
            suggestions.insert(new Word(nodeActual.getWord(), ((WordNode) nodeActual).getSearches()));
        }
        Node[] actualNodes = nodeActual.getChilds().toArray(new Node[nodeActual.getChilds().getSize()]);
        for(Node n: actualNodes) {
            searchForWordAux(n, suggestions);
        }
        return suggestions;
    }

    private LinkedList<String> searchForWord(Node nodeActual, LinkedList<String> suggestions) {
        if(nodeActual instanceof WordNode) {
            suggestions.insert(nodeActual.getWord());
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
