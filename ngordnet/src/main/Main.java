package main;

import browser.NgordnetServer;
import ngrams.NGramMap;
import wordnet.WordNet;

public class Main {
    public static void main(String[] args) {
        NgordnetServer hns = new NgordnetServer();

        String wordFile = "./data/ngrams/top_14377_words.csv";
        String countFile = "./data/ngrams/total_counts.csv";
        NGramMap ngm = new NGramMap(wordFile, countFile);

        String synsetFilename = "./data/wordnet/synsets.txt";
        String hyponymFilename = "./data/wordnet/hyponyms.txt";

        WordNet wnet = new WordNet(synsetFilename, hyponymFilename);

        hns.startUp();
        hns.register("history", new HistoryHandler(ngm));
        hns.register("historytext", new HistoryTextHandler(ngm));
        hns.register("hyponyms", new HyponymsHandler(wnet, ngm));

        System.out.println("Finished server startup! Visit http://localhost:4567/ngordnet.html");
    }
}
