package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import wordnet.WordNet;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private WordNet wnet;
    private NGramMap ngm;

    public HyponymsHandler(WordNet wnet, NGramMap ngm) {
        this.wnet = wnet;
        this.ngm = ngm;
    }

    public String handle(NgordnetQuery q) {
        List<String> lstWords = wnet.getHyponyms(q.words()).stream().sorted().toList();

        int k = q.k();
        if (k == 0) {
            return lstWords.toString();
        }

        int startYear = q.startYear();
        int endYear = q.endYear();

        HashMap<String, Double> popularity = new HashMap<>();
        TimeSeries ts;
        double totalCount;
        for (String word : lstWords) {
            ts = ngm.countHistory(word, startYear, endYear);
            if (!ts.isEmpty()) {

                totalCount = ts.values().stream().reduce(0.0, (a, b) -> (a + b));
                popularity.put(word, totalCount);
            }
        }
        PriorityQueue<String> pq = new PriorityQueue<String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (popularity.get(o1) > popularity.get(o2)) {
                    return 1;
                } else if (popularity.get(o1) < popularity.get(o2)) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        for (String word : popularity.keySet()) {
            pq.add(word);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        List<String> out = new ArrayList<String>();
        while (!pq.isEmpty()) {
            out.add(pq.peek());
            pq.poll();
        }

        return out.stream().sorted().toList().toString();
    }

}
