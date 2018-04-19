package com.eviabs.dicts.ApiClients;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ApiConsts {

    // Server/client consts
    public static final int ERROR_CODE_NO_ERROR = 0; // query executed successfully
    public static final int ERROR_CODE_SERVER_ERROR = 1; //remote server error
    public static final int ERROR_CODE_NO_RESULTS = 2; // no results
    public static final int ERROR_CODE_NO_SUCH_DIC = 3; // no such dictionary
    public static final int ERROR_CODE_BAD_QUERY = 4; // bad query
    public static final int ERROR_CODE_UNEXPECTED_ERROR = 5; // unexpected error
    public static final int ERROR_CODE_UNINITIALIZED = 1000; // indicates that the error valuse hasn't been set yet
    public static final int ERROR_CODE_SEARCHING = 666; // searching

    // Server URL
    public static String SERVER_BASE_URL = "https://web-dicts.herokuapp.com/";

    // Query Bundle
    public static final String QUERY_BUNDLE_TERM = "term";


    public static final String SEARCH_PROVIDER_AUTOCOMPLETE = "autocomplete";
    public static final String DICTIONARY_IMAGES = "Images";
    public static final String DICTIONARY_MORIFX = "Morfix";
    public static final String DICTIONARY_URBAN_DICTIONARY = "Urban Dictionary";
    public static final String DICTIONARY_WIKIPEDIA = "Wikipedia";

//    public static final String[] SEARCH_PROVIDERS = {
//            DICTIONARY_IMAGES,
//            DICTIONARY_MORIFX,
//    };

    public static final String[] SEARCH_PROVIDERS = {
            DICTIONARY_IMAGES,
            DICTIONARY_MORIFX,
            DICTIONARY_WIKIPEDIA,
            DICTIONARY_URBAN_DICTIONARY
    };
}