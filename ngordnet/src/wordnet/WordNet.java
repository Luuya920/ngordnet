package wordnet;

import com.google.common.collect.Sets;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {
    private HashMap<Integer, Synset> synsetMap = new HashMap<>();
    private DirectedGraph graph;

    public WordNet(String synsetFilename, String hyponymFilename) {
        In in = new In(synsetFilename);
        String[] tokens;
        String line;
        Integer id;

        while (!in.isEmpty()) {
            line = in.readLine();
            tokens = line.split(",");
            id = Integer.parseInt(tokens[0]);
            Set<String> st = new HashSet<String>(Arrays.asList(tokens[1].split(" ")));

            synsetMap.put(id, new Synset(st, tokens[2]));
        }

        in = new In(hyponymFilename);
        graph = new DirectedGraph();

        while (!in.isEmpty()) {
            line = in.readLine();
            tokens = line.split(",");
            id = Integer.parseInt(tokens[0]);

            for (int i = 1; i < tokens.length; i++) {
                graph.addEdge(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[i]));
            }
        }

    }

    private Set<String> getHyponymsOneWord(String word) {
        Set<String> outputs = new HashSet<String>();

        for (Integer id : synsetMap.keySet()) {
            if (synsetMap.get(id).containsWord(word)) {
                List<Integer> childrens = graph.getChildrens(id);
                for (Integer child : childrens) {
                    outputs.addAll(synsetMap.get(child).getSynonyms());
                }
            }
        }
        return outputs;
    }

    public Set<String> getHyponyms(List<String> word) {
        Set<String> tmp = getHyponymsOneWord(word.get(0));

        for (int i = 1; i < word.size(); i++) {
            tmp.retainAll(getHyponymsOneWord(word.get(i)));
        }

        return tmp;
    }

}
