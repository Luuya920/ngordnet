package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 * <p>
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private HashMap<String, TimeSeries> mp = new HashMap<String, TimeSeries>();
    private TimeSeries totalCount = new TimeSeries();

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {

        In in = new In(wordsFilename);
        String[] tokens;
        String line;
        int year;
        double count;

        while (!in.isEmpty()) {
            line = in.readLine();
            tokens = line.split("[\t]+");
            year = Integer.parseInt(tokens[1]);
            count = Double.parseDouble(tokens[2]);

            if (mp.containsKey(tokens[0])) {
                mp.get(tokens[0]).put(year, count);
            } else {
                TimeSeries ts = new TimeSeries();
                ts.put(year, count);
                mp.put(tokens[0], ts);
            }
        }

        in = new In(countsFilename);
        while (!in.isEmpty()) {
            line = in.readLine();
            tokens = line.split(",");
            year = Integer.parseInt(tokens[0]);
            count = Double.parseDouble(tokens[1]);
            totalCount.put(year, count);
        }


    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries ts = new TimeSeries();
        if (mp.containsKey(word)) {
            ts = new TimeSeries(mp.get(word), startYear, endYear);
        }

        return ts;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        TimeSeries ts = new TimeSeries();
        if (mp.containsKey(word)) {
            ts = new TimeSeries(mp.get(word), MIN_YEAR, MAX_YEAR);
        }

        return ts;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(totalCount, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        return countHistory(word, startYear, endYear).dividedBy(totalCount);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        return countHistory(word, MIN_YEAR, MAX_YEAR).dividedBy(totalCount);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        TimeSeries ts = new TimeSeries();

        for (String word : words) {
            if (mp.containsKey(word)) {
                ts = ts.plus(weightHistory(word, startYear, endYear));
            }
        }

        return ts;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries ts = new TimeSeries();

        for (String word : words) {
            if (mp.containsKey(word)) {
                ts = ts.plus(weightHistory(word));
            }
        }

        return ts;
    }

}
