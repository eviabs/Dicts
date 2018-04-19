package com.eviabs.dicts.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eviabs.dicts.Activities.MainActivity;
import com.eviabs.dicts.ApiClients.ApiConsts;
import com.eviabs.dicts.R;
import com.eviabs.dicts.SearchProviders.SearchProvider;
import com.eviabs.dicts.SearchProviders.SearchProvidersManager;
import com.eviabs.dicts.Utils.LocalPreferences;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchResultsFragment extends Fragment {

    private String TAG = SearchResultsFragment.class.getSimpleName() + "@asw!";
    private SearchProvidersManager searchProvidersManager = null;
    private View view = null;

    /**
     * Default empty Ctor
     */
    public SearchResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // We are holding a static member called view. We make sure to inflate the view ONLY ONCE.
        // We also remove the view from it's parent if needed.
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search_results, container, false);

            // the containers of the search fragment
            LinearLayout imagesLayout = view.findViewById(R.id.container_images);
            LinearLayout dictionariesLayout = view.findViewById(R.id.container_dictionaries);

            searchProvidersManager = new SearchProvidersManager(getActivity(), getLocalPreferences());

            // init all search providers
            for (String searchProviderName: ApiConsts.SEARCH_PROVIDERS) {
                searchProvidersManager.addSearchProvider(searchProviderName, LinearLayoutManager.VERTICAL, dictionariesLayout);
            }

            // override the images' search providers object
            searchProvidersManager.addSearchProvider(ApiConsts.DICTIONARY_IMAGES, LinearLayoutManager.HORIZONTAL, imagesLayout, new SearchProvider.QueryBundleInterface() {
                @Override
                public Bundle createQueryBundle(String term) {
                    Bundle queryBundle = new Bundle();
                    queryBundle.putString(ApiConsts.QUERY_BUNDLE_TERM, term);
                    queryBundle.putInt("images_num", getLocalPreferences().getNumOfImagesToShow());
                    return queryBundle;
                }
            });

            searchProvidersManager.showAvailableSearchProviders();

        } else {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
        }
        return view;
    }

    /**
     * The main search function.
     * This function searches for the query in all of the search providers that were selected in the settings.
     * We also hide the search providers that were removed by the user.
     *
     * @param query the search term.
     */
    public void search(String query) {

        // hide the startup warnings
        safeSetVisibility(view.findViewById(R.id.container_dictionaries_warning), View.GONE);
        safeSetVisibility(view.findViewById(R.id.container_images_warning), View.GONE);

        searchProvidersManager.showAvailableSearchProviders();
        searchProvidersManager.search(query, getServerURL());
    }

    /**
     * Get the LocalPreferences object from the host activity.
     * @return local LocalPreferences object.
     */
    public LocalPreferences getLocalPreferences() {
        return ((MainActivity) getActivity()).getLocalPreferences();
    }

    /**
     * Get the server URL (costum or default)
     * @return the server URL
     */
    public String getServerURL() {
        if (getLocalPreferences().isCustomServer()) {
            return getLocalPreferences().getCustonURLServer();
        }

        return ApiConsts.SERVER_BASE_URL;
    }

    /**
     * Some objects might not be set when this function is called, so we make sure
     * that the setVisivility function will not be invoked from null.
     *
     * @param view the view
     * @param visibility the visibility value
     */
    public void safeSetVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }
}
