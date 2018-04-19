package com.eviabs.dicts.SearchProviders;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.eviabs.dicts.Adapters.TermAdapter;
import com.eviabs.dicts.Adapters.TermAdapterFactory;
import com.eviabs.dicts.ApiClients.ApiConsts;
import com.eviabs.dicts.ApiClients.ClientFactory;
import com.eviabs.dicts.Fragments.SearchResultsFragment;
import com.eviabs.dicts.Utils.LocalPreferences;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class manages the search providers.
 * After adding search providers, you can use the manager to perform the searches.
 *
 */
public class SearchProvidersManager {

    private String TAG = SearchResultsFragment.class.getSimpleName() + "@asw!";
    private HashMap<String, SearchProvider> searchProviders = new HashMap<>();
    private Context context = null;
    private FragmentActivity activity = null;
    private LocalPreferences localPreferences = null;

    /**
     * Ctor
     *
     * @param context application context
     * @param localPreferences localPreferences object
     */
    public SearchProvidersManager(Context context, LocalPreferences localPreferences) {
        this.context = context;
        this.activity = (FragmentActivity) context;
        this.localPreferences = localPreferences;
    }

    /**
     * Adds a search provider to the manager
     *
     * @param name name
     * @param orientation orientation
     * @param searchProviderLayout searchProviderLayout
     * @param queryBundleInterface queryBundleInterface
     */
    public void addSearchProvider(String name, int orientation, LinearLayout searchProviderLayout, SearchProvider.QueryBundleInterface queryBundleInterface) {
        searchProviders.put(name, new SearchProvider(name, null, orientation, searchProviderLayout, queryBundleInterface));
    }

    /**
     * Adds a search provider to the manager
     *
     * @param name name
     * @param orientation orientation
     * @param searchProviderLayout searchProviderLayout
     */
    public void addSearchProvider(String name, int orientation, LinearLayout searchProviderLayout) {
        searchProviders.put(name, new SearchProvider(name, null, orientation, searchProviderLayout));
    }

    /**
     * Some objects might not be set when this function is called, so we make sure
     * that the setVisivility function will not be invoked from null.
     *
     * @param view the view
     * @param visibility the visibility value
     */
    private void safeSetVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    /**
     * This function shows all of the search providers that the user selected, and hides the ones
     * that were removed.
     */
    public void showAvailableSearchProviders() {

        for (SearchProvider searchProvider: searchProviders.values()) {
            safeSetVisibility(searchProvider.getRecyclerView(), View.VISIBLE);

            if (!localPreferences.getSearchProviders().contains(searchProvider.getName())) {
                safeSetVisibility(searchProvider.getRecyclerView(), View.GONE);
            }
        }
    }

    /**
     * The main search function.
     * This function searches for the query in all of the search providers that were selected in the settings.
     * We also hide the search providers that were removed by the user.
     *
     * @param query the search term.
     */
    public void search(String query, String serveUrl) {

        // we iterate over SEARCH_PROVIDERS to keep the right order of the providers (because getLocalPreferences().getSearchProviders() uses an unordered map).
        for (String searchProviderName: ApiConsts.SEARCH_PROVIDERS) {
            if (localPreferences.getSearchProviders().contains(searchProviderName)) {
                searchAsync(query, searchProviders.get(searchProviderName), serveUrl);
            }
        }
    }

    /**
     * Search the web asynchronously.
     * @param term the search term
     * @param searchProvider the search provider object
     */
    private void searchAsync(final String term, final SearchProvider searchProvider, final String serverUrl) {

        final Context activityContext = this.activity;
        final View.OnClickListener retryClick = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                searchAsync(term, searchProvider, serverUrl);
            }
        };

        TermAdapter termAdapter = TermAdapterFactory.getTermAdapter(searchProvider.getName(), activityContext, ApiConsts.ERROR_CODE_SEARCHING, null, retryClick);
        setupRecyclerView(searchProvider, termAdapter);

        Call<ResponseBody> call = ClientFactory.getCall(searchProvider.getQueryBundle(term), searchProvider.getName(), serverUrl);
        if (call == null) {
            Log.d(TAG, "ClientFactory returned null... Check that the factory can crate the appropriate objects.");
            return;
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TermAdapter termAdapter = TermAdapterFactory.getTermAdapter(searchProvider.getName(), activityContext, ApiConsts.ERROR_CODE_UNINITIALIZED, response.body(), retryClick);
                setupRecyclerView(searchProvider, termAdapter);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TermAdapter termAdapter = TermAdapterFactory.getTermAdapter(searchProvider.getName(), activityContext, ApiConsts.ERROR_CODE_SERVER_ERROR, null, retryClick);
                setupRecyclerView(searchProvider, termAdapter);

            }
        });
    }

    /**
     * This function sets up all of the recyclerView objects.
     * We swap adapters if needed, and assign each recyclerView only once.
     *
     * @param searchProvider the search provider object
     * @param adapter the current adapter of the recyclerView
     */
    private void setupRecyclerView(SearchProvider searchProvider, RecyclerView.Adapter adapter){

        if (searchProvider.getRecyclerView() == null) {

            searchProvider.setRecyclerView(new RecyclerView(context));
            searchProvider.getSearchProviderLayout().addView(searchProvider.getRecyclerView());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, searchProvider.getOrientation(), false);
            searchProvider.getRecyclerView().setLayoutManager(mLayoutManager);
            searchProvider.getRecyclerView().setItemAnimator(new DefaultItemAnimator());
            searchProvider.getRecyclerView().setAdapter(adapter);
            searchProvider.getRecyclerView().setNestedScrollingEnabled(false);

        } else {
            searchProvider.getRecyclerView().swapAdapter(adapter, false);
        }
    }
}
