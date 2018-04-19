package com.eviabs.dicts.SearchProviders.Autocomplete;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.util.Arrays;

public class GoogleAutocomplete {

    private static String DELIMITER = ",";
    private static String BAD_CHARS = "\\[|\\]|\"";
    private String suggestionsString = null;

    public GoogleAutocomplete(String suggestionsString) {
        this.suggestionsString = suggestionsString;
    }

    public String[] getSuggestions(int maxNumOfResults) throws IOException {
        if (suggestionsString != null) {
            // clean the response and split to array of strings
            String cleanedBodyStr = StringEscapeUtils.unescapeJava(suggestionsString.replaceAll(BAD_CHARS, ""));
            String[] arrSuggestions = cleanedBodyStr.split(DELIMITER);

            // remove the first (and duplicate) result
            if (arrSuggestions.length > 0) {
                arrSuggestions = Arrays.copyOfRange(arrSuggestions, 1, arrSuggestions.length);
            }

            // force max num of suggestions
            if (arrSuggestions.length > maxNumOfResults) {
                arrSuggestions = Arrays.copyOfRange(arrSuggestions, 0, maxNumOfResults);
            }

            // sort and return
            Arrays.sort(arrSuggestions);

            return arrSuggestions;
        }

        return new String[] {};
    }
}
