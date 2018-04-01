package com.eviabs.dicts.ApiClients;

import com.eviabs.dicts.Dictionaries.Wikipedia.WikipediaResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WikipediaClient {

    @GET("/dic/wikipedia")
    Call<WikipediaResults> getDefinition(@Query("t") String term);
}