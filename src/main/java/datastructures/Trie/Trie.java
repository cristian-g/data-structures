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
        char[] charArray = username.toCharArray();
        String auxKey = "";
        LinkedList<Node> actualNodes = nodes;
        for(int i = 0; i < charArray.length; i++) {
            auxKey += charArray[i];
            for(int j = 0; j < actualNodes.getSize(); j++) {
                String key = actualNodes.getByKey(j).getKey();
                char[] charkey = username.toCharArray();
                if(charArray[i] == charkey[i]) {
                    actualNodes = actualNodes.getByKey(j).getChilds();
                }
            }
            //Arriba aquí si ha reccoregut els fills i cap té la lletra.
            //Per tant, creo el node necessari i l'afegeixo.
            if(i == charArray.length - 1) {
                WordNode newNode = new WordNode(auxKey);
                actualNodes.insert(newNode);    //Això modifica el actualNodes però no la LinkedList original crec. No sé com agafar la referència.
            } else {
                Node newNode = new Node(auxKey);
                actualNodes.insert(newNode);    //Això modifica el actualNodes però no la LinkedList original crec. No sé com agafar la referència.
            }
        }
    }

    public void deleteUser(Node actualNode, String username) {
        Node[] children = actualNode.getChilds().toArray(new Node[actualNode.getChilds().getSize()]);
        for(Node n: children) {
            //Si el node actual és el node que conté el username:
            if(n.getKey().equals(username) && (n instanceof WordNode)) {
                if(n.getChilds() == null) {
                    //Eliminar referència al Node des del pare.
                    //Si el pare no té fills s'ha d'anar eliminant recursivament cap a dalt fins que tingui fills??
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
        for(int i = 0; i < usernames.getSize(); i++) {
            addUser(usernames.getByKey(i));
        }
    }

    public String[] getSuggestions(String partialName) {
        return null;
    }

    public void limitMemory(int words) {
        this.words = words;
    }
}
