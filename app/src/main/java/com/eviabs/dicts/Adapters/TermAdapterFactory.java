package com.eviabs.dicts.Adapters;

import android.content.Context;
import android.view.View;

import com.eviabs.dicts.ApiClients.ApiConsts;

import java.util.HashSet;

import okhttp3.ResponseBody;

/**
 * A factory for TermAdapters
 */
public class TermAdapterFactory {


    public static TermAdapter getTermAdapter(String searchProviderName, Context mContext, int error, ResponseBody responseBody, View.OnClickListener retryOnClickListener) {
        switch (searchProviderName) {

            case ApiConsts.DICTIONARY_IMAGES:
                return new QwantImagesAdapter(mContext, error, responseBody, retryOnClickListener);

            case ApiConsts.DICTIONARY_MORIFX:
                return new MorfixTermAdapter(mContext, error, responseBody, retryOnClickListener);

            case ApiConsts.DICTIONARY_URBAN_DICTIONARY:
                return new UrbanDictionaryTermAdapter(mContext, error, responseBody, retryOnClickListener);

            case ApiConsts.DICTIONARY_WIKIPEDIA:
                return new WikipediaTermAdapter(mContext, error, responseBody, retryOnClickListener);
        }

        return null;
    }
}
