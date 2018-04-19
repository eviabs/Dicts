package com.eviabs.dicts.SearchProviders;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.eviabs.dicts.ApiClients.ApiConsts;
import com.eviabs.dicts.Fragments.SearchResultsFragment;

public class SearchProvider {

    public interface QueryBundleInterface {
        Bundle createQueryBundle(String term);
    }

    private String name;
    private RecyclerView recyclerView;
    private int orientation;
    private LinearLayout searchProviderLayout;
    private QueryBundleInterface queryBundleInterface;

    public SearchProvider(String name, RecyclerView recyclerView, int orientation, LinearLayout searchProviderLayout, QueryBundleInterface queryBundleInterface) {
        this.name = name;
        this.recyclerView = recyclerView;
        this.orientation = orientation;
        this.searchProviderLayout = searchProviderLayout;
        this.queryBundleInterface = queryBundleInterface;
    }

    public SearchProvider(String name, RecyclerView recyclerView, int orientation, LinearLayout searchProviderLayout) {

        // set the default createQueryBundle function.
        this(name, recyclerView, orientation, searchProviderLayout, new QueryBundleInterface() {
            @Override
            public Bundle createQueryBundle(String term) {
                Bundle queryBundle = new Bundle();
                queryBundle.putString(ApiConsts.QUERY_BUNDLE_TERM, term);
                return queryBundle;
            }
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public LinearLayout getSearchProviderLayout() {
        return searchProviderLayout;
    }

    public void setSearchProviderLayout(LinearLayout searchProviderLayout) {
        this.searchProviderLayout = searchProviderLayout;
    }

    public QueryBundleInterface getQueryBundleInterface() {
        return queryBundleInterface;
    }

    public void setQueryBundleInterface(QueryBundleInterface queryBundleInterface) {
        this.queryBundleInterface = queryBundleInterface;
    }

    public Bundle getQueryBundle(String term) {
        return queryBundleInterface.createQueryBundle(term);
    }
}
