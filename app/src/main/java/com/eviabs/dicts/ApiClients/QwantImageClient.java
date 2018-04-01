package com.eviabs.dicts.ApiClients;

import com.eviabs.dicts.Dictionaries.Qwant.QwantImageResults;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface QwantImageClient {

    @GET("/dic/images")
    Call<QwantImageResults> getImages(@Query("t") String term, @Query("count") int count);
}