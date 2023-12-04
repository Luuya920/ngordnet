package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import org.knowm.xchart.XYChart;
import plotting.Plotter;

import java.util.ArrayList;

public class HistoryHandler extends NgordnetQueryHandler {
    private NGramMap ngrm;

    public HistoryHandler(NGramMap map) {
        ngrm = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        ArrayList<String> words = new ArrayList<>(q.words());
        ArrayList<TimeSeries> lts = new ArrayList<>();

        for (String word : words) {
            lts.add(ngrm.weightHistory(word, q.startYear(), q.endYear()));
        }

        XYChart chart = Plotter.generateTimeSeriesChart(words, lts);

        return Plotter.encodeChartAsString(chart);

    }
}
