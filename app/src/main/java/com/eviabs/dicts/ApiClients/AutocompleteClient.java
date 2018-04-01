package com.eviabs.dicts.ApiClients;

import com.eviabs.dicts.Dictionaries.Morfix.MorfixResults;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AutocompleteClient {

    String BASE_URL = "http://suggestqueries.google.com";
    String DELIMITER = ",";
    String BAD_CHARS = "\\[|\\]|\"";
    String CLIENT_JSON = "firefox";
    String LANGUAGE = "iw";

    @GET("http://suggestqueries.google.com/complete/search")
    Call<ResponseBody> getSuggestions(@Query("q") String term, @Query("client") String client, @Query("hl") String lang);
}