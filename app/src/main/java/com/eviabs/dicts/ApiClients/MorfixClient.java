package com.eviabs.dicts.ApiClients;

import com.eviabs.dicts.Dictionaries.Morfix.MorfixResults;
import com.eviabs.dicts.Dictionaries.Wikipedia.WikipediaResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MorfixClient {

    @GET("/dic/morfix")
    Call<MorfixResults> getDefinitions(@Query("t") String term);
}