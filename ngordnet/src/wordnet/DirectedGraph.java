package wordnet;

import java.util.*;

public class DirectedGraph {
    private HashMap<Integer, ArrayList<Integer>> edges = new HashMap<>();

    public void addEdge(Integer src, Integer dst) {
        if (edges.containsKey(src)) {
            edges.get(src).add(dst);
        } else {
            ArrayList<Integer> lst = new ArrayList<>();
            lst.add(dst);
            edges.put(src, lst);
        }
    }

    public List<Integer> getChildrens(Integer id) {
        ArrayList<Integer> tmp = new ArrayList<>();
        tmp.add(id);

        if (!edges.containsKey(id)) {
            return tmp;
        }
        for (Integer src : edges.get(id)) {
            tmp.addAll(getChildrens(src));
        }
        return tmp;
    }
}
