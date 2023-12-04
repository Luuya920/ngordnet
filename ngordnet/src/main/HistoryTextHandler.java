package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private final NGramMap mp;

    public HistoryTextHandler(NGramMap ngm) {
        mp = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {

        StringBuilder response = new StringBuilder();
        for (String word : q.words()) {
            if (mp.weightHistory(word, q.startYear(), q.endYear()) != null) {
                response.append(word).append(": ");
                response.append(mp.weightHistory(word, q.startYear(), q.endYear()).toString()).append("\n");
            }
        }

        return response.toString();
    }
}
