package datastructures.Trie;

public class Word implements Comparable<Word>{
    private String word;
    private int searches;

    public Word(String word, int searches) {
        this.word = word;
        this.searches = searches;
    }

    public String getWord() {
        return word;
    }

    public int getSearches() {
        return searches;
    }

    @Override
    public int compareTo(Word o) {
        if(this.searches > o.getSearches()) {
            return 1;
        }
        if (this.searches < o.getSearches()) {
            return -1;
        }
        return 0;
    }
}
