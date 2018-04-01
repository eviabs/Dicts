package com.eviabs.dicts.ApiClients;

import com.eviabs.dicts.Dictionaries.Qwant.QwantImageResults;
import com.eviabs.dicts.Dictionaries.UrbanDictionary.UrbanDictionaryResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UrbanDictionaryClient {

    @GET("/dic/urban")
    Call<UrbanDictionaryResults> getDefinitions(@Query("t") String term);
}