package wordnet;

import java.util.Set;

public class Synset {
    private Set<String> synonyms;
    private String meaning;

    public Synset(Set<String> synonyms, String meaning) {
        this.synonyms = synonyms;
        this.meaning = meaning;
    }

    public boolean containsWord(String word) {
        return synonyms.contains(word);
    }

    public Set<String> getSynonyms() {
        return synonyms;
    }
}
