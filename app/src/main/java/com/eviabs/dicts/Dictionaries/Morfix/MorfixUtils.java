package com.eviabs.dicts.Dictionaries.Morfix;

import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides some utils to handle Morfix objects.
 * All of the Morfix objects are stripped as-is from the web api and don't include any helpers/utils
 * inside their classes.
 */
public class MorfixUtils {

    @NonNull
    public static String arrayOfStringsToString(List<String> arr, String delimiter) {
        if (arr == null || arr.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.size(); i++) {
            builder.append(arr.get(i));

            // don't add {delimiter} to the last item
            if (i < arr.size() - 1) {
                builder.append(delimiter);
            }
        }
        return builder.toString();
    }

    @NonNull
    public static String inflectionsToString(List<Inflection> arr) {

        // extract actual inflections
        ArrayList<String> arrInflection = new ArrayList<>();
        for (Inflection inflection : arr) {
            arrInflection.add(inflection.getText());

        }

        return arrayOfStringsToString(arrInflection, ", ");
    }

    @NonNull
    public static String examplesToString(List<String> arr) {
        return arrayOfStringsToString(arr, "<br><br>");
    }

    @NonNull
    public static String synonymsToString(List<String> arr) {
        return arrayOfStringsToString(arr, ", ");
    }

    public static int getDirection(MorfixResults results) {
        if (results != null && results.getTranslationType().equals("ToEnglish")) {
            return View.LAYOUT_DIRECTION_RTL;
        }
        return View.LAYOUT_DIRECTION_LTR;
    }
}
