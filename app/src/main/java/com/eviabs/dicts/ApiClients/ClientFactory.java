package com.eviabs.dicts.ApiClients;

import android.os.Bundle;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class ClientFactory {

    /**
     * Retrofit's Client interface.
     * Add your api calls here.
     */
    interface Client {
        @GET("/dic/milog")
        Call<ResponseBody> getMilogDefinitions(@Query("t") String term);

        @GET("/dic/morfix")
        Call<ResponseBody> getMorfixDefinitions(@Query("t") String term);

        @GET("/dic/urban")
        Call<ResponseBody> getUrbanDictionaryDefinitions(@Query("t") String term);

        @GET("/dic/wikipedia")
        Call<ResponseBody> getWikipediaDefinitions(@Query("t") String term);

        @GET("/dic/images")
        Call<ResponseBody> getQwantImages(@Query("t") String term, @Query("count") int count);

        @GET("http://suggestqueries.google.com/complete/search")
        Call<ResponseBody> getAutoCompleteSuggestions(@Query("q") String term, @Query("client") String client, @Query("hl") String lang);
    }

    /**
     * Call Factory
     *
     * Make sure you "open" the bundle as required.
     * By default the bundle contains only term string.
     *
     * @param queryBundle the query params
     * @param searchProvider the search provider to create
     * @param url retrofit url
     * @return a new Call object
     */
    public static Call<ResponseBody> getCall(Bundle queryBundle, String searchProvider, String url) {
        switch (searchProvider) {
            case ApiConsts.DICTIONARY_IMAGES:
                return getClient(url).getQwantImages(queryBundle.getString(ApiConsts.QUERY_BUNDLE_TERM), queryBundle.getInt("images_num"));

            case ApiConsts.DICTIONARY_MORIFX:
                return getClient(url).getMorfixDefinitions(queryBundle.getString(ApiConsts.QUERY_BUNDLE_TERM));

            case ApiConsts.DICTIONARY_MILOG:
                return getClient(url).getMilogDefinitions(queryBundle.getString(ApiConsts.QUERY_BUNDLE_TERM));

            case ApiConsts.DICTIONARY_URBAN_DICTIONARY:
                return getClient(url).getUrbanDictionaryDefinitions(queryBundle.getString(ApiConsts.QUERY_BUNDLE_TERM));

            case ApiConsts.DICTIONARY_WIKIPEDIA:
                return getClient(url).getWikipediaDefinitions(queryBundle.getString(ApiConsts.QUERY_BUNDLE_TERM));

            case ApiConsts.SEARCH_PROVIDER_AUTOCOMPLETE:
                // We need to change the retrofit's url to google's server which is much faster than other alternatives.
                return getClient("http://suggestqueries.google.com/complete/search/").getAutoCompleteSuggestions(queryBundle.getString("term"), "firefox", "iw");
        }

        return null;
    }

    /**
     * Get the retrofit object using the user's server (custom or default server).
     * @return the retrofit object.
     */
    private static Retrofit getRetrofit(String url) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build();
    }

    /**
     * Get a client created by the retrofit class
     * @param url retrofit's url
     * @return a Client object
     */
    private static Client getClient(String url) {
        return getRetrofit(url).create(Client.class);
    }
}