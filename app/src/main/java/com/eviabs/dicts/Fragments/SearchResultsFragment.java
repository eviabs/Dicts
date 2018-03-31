package com.eviabs.dicts.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eviabs.dicts.Activities.MainActivity;
import com.eviabs.dicts.Adapters.MorfixTermAdapter;
import com.eviabs.dicts.Adapters.QwantImagesAdapter;
import com.eviabs.dicts.Adapters.UrbanDictionaryTermAdapter;
import com.eviabs.dicts.Adapters.WikipediaTermAdapter;
import com.eviabs.dicts.ApiClients.ApiConsts;
import com.eviabs.dicts.ApiClients.MorfixClient;
import com.eviabs.dicts.ApiClients.QwantImageClient;
import com.eviabs.dicts.ApiClients.UrbanDictionaryClient;
import com.eviabs.dicts.ApiClients.WikipediaClient;
import com.eviabs.dicts.Dictionaries.Morfix.MorfixResults;
import com.eviabs.dicts.Dictionaries.Qwant.QwantImageResults;
import com.eviabs.dicts.Dictionaries.UrbanDictionary.UrbanDictionaryResults;
import com.eviabs.dicts.Dictionaries.Wikipedia.WikipediaResults;
import com.eviabs.dicts.R;
import com.eviabs.dicts.Utils.LocalPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.eviabs.dicts.ApiClients.ApiConsts.ERROR_CODE_NO_ERROR;
import static com.eviabs.dicts.ApiClients.ApiConsts.ERROR_CODE_NO_RESULTS;
import static com.eviabs.dicts.ApiClients.ApiConsts.SERVER_BASE_URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchResultsFragment extends Fragment {

    private static View view = null;

    private QwantImagesAdapter qwantImagesAdapter = null;
    private RecyclerView qwantRecyclerView = null;

    private UrbanDictionaryTermAdapter urbanDictionaryTermAdapter = null;
    private RecyclerView urbanDictionaryRecyclerView = null;
    private LinearLayout urbanDictionaryRecyclerViewLayout = null;

    private WikipediaTermAdapter wikipediaTermAdapter = null;
    private RecyclerView wikipediaRecyclerView = null;
    private LinearLayout wikipediaRecyclerViewLayout = null;

    private MorfixTermAdapter morfixTermAdapter = null;
    private RecyclerView morfixRecyclerView = null;
    private LinearLayout morfixRecyclerViewLayout = null;

    private LinearLayout containerImagesLayout = null;
    private LinearLayout containerImagesWarningLayout = null;
    private LinearLayout containerImagesWarningRetryLayout = null;
    private LinearLayout containerImagesRecyclerLayout = null;
    private ImageView containerImagesWarningImage = null;
    private TextView containerImagesWarningText = null;


    private LinearLayout containerDictionariesWarningLayout = null;
    private ImageView containerDictionariesWarningImage = null;
    private TextView containerDictionariesWarningText = null;

    private NestedScrollView scrollViewSearch = null;

    public SearchResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search_results, container, false);

            containerImagesLayout = view.findViewById(R.id.container_images);
            containerImagesWarningLayout = view.findViewById(R.id.container_images_warning);
            containerImagesWarningRetryLayout = view.findViewById(R.id.container_images_warning_retry);
            containerImagesRecyclerLayout = view.findViewById(R.id.container_recycler_view_list_images);
            containerImagesWarningImage = view.findViewById(R.id.container_images_warning_image);
            containerImagesWarningText = view.findViewById(R.id.container_images_warning_text);

            containerDictionariesWarningLayout = view.findViewById(R.id.container_dictionaries_warning);
            containerDictionariesWarningImage = view.findViewById(R.id.container_dictionaries_warning_image);
            containerDictionariesWarningText = view.findViewById(R.id.container_dictionaries_text);

            scrollViewSearch = view.findViewById(R.id.scroll_view_search);

            morfixRecyclerViewLayout = view.findViewById(R.id.recycler_view_card_morfix_term_layout);
            urbanDictionaryRecyclerViewLayout = view.findViewById(R.id.recycler_view_card_urban_dictionary_term_layout);
            wikipediaRecyclerViewLayout = view.findViewById(R.id.recycler_view_card_wikipedia_term_layout);

            showAvailableSearchProviders();
        } else {
            // todo: check if java.lang.IllegalStateException: The specified child already has a parent was fixed

            ((ViewGroup) view.getParent()).removeView(view);
    }
        return view;
    }

    public void search(String query) {

        containerDictionariesWarningLayout.setVisibility(View.GONE);
        containerDictionariesWarningImage.setVisibility(View.GONE);
        containerDictionariesWarningText.setVisibility(View.GONE);

        showAvailableSearchProviders();

        if (getLocalPreferences().isCustomSearchProvider()) {
            if (getLocalPreferences().isCustomSearchProviderImages()) {
                searchQwantImagesAsync(query);
            }

            if (getLocalPreferences().isCustomSearchProviderUrbanDictionary()) {
                searchUrbanDictionaryAsync(query);
            }

            if (getLocalPreferences().isCustomSearchProviderWikipedia()) {
                searchWikipediaAsync(query);
            }

            if (getLocalPreferences().isCustomSearchProviderMorfix()) {
                searchMorfixAsync(query);
            }
        } else {
            searchQwantImagesAsync(query);
            searchUrbanDictionaryAsync(query);
            searchWikipediaAsync(query);
            searchMorfixAsync(query);
        }
    }

    private void showAvailableSearchProviders() {
        containerImagesLayout.setVisibility(View.VISIBLE);
        safeSetVisivility(urbanDictionaryRecyclerViewLayout, View.VISIBLE);
        safeSetVisivility(wikipediaRecyclerViewLayout, View.VISIBLE);
        safeSetVisivility(morfixRecyclerViewLayout, View.VISIBLE);

        if (getLocalPreferences().isCustomSearchProvider()) {

            if (!getLocalPreferences().isCustomSearchProviderImages()) {
                containerImagesLayout.setVisibility(View.GONE);
            }

            if (!getLocalPreferences().isCustomSearchProviderUrbanDictionary()) {
                safeSetVisivility(urbanDictionaryRecyclerViewLayout, View.GONE);
            }

            if (!getLocalPreferences().isCustomSearchProviderWikipedia()) {
                safeSetVisivility(wikipediaRecyclerViewLayout, View.GONE);
            }

            if (!getLocalPreferences().isCustomSearchProviderMorfix()) {
                safeSetVisivility(morfixRecyclerViewLayout, View.GONE);
            }
        }
    }

    private void searchQwantImagesAsync(final String term) {
        Retrofit retrofit = getRetrofit();
        QwantImageClient client = retrofit.create(QwantImageClient.class);
        Call<QwantImageResults> call = client.getImages(term, getLocalPreferences().getNumOfImagesToShow());

        setQwantWarningPanel(term, true, false, R.drawable.searching_images, R.string.images_warning_searching);

        call.enqueue(new Callback<QwantImageResults>() {
            @Override
            public void onResponse(Call<QwantImageResults> call, Response<QwantImageResults> response) {
                qwantImagesAdapter = new QwantImagesAdapter(getActivity(), response.body());
                setupRecyclerView(qwantRecyclerView, R.id.recycler_view_list_images, qwantImagesAdapter, LinearLayoutManager.HORIZONTAL);

                switch (response.body().getError()){

                    case ERROR_CODE_NO_ERROR:
                        setQwantWarningPanel(term, false, false, 0, 0);
                        break;

                    case ERROR_CODE_NO_RESULTS:
                        setQwantWarningPanel(term, true, false, R.drawable.no_images_found, R.string.images_warning_no_images);
                        break;

                    default:
                        setQwantWarningPanel(term, true, true, R.drawable.server_error, R.string.images_warning_server_error);
                }
            }

            @Override
            public void onFailure(Call<QwantImageResults> call, Throwable t) {
                setQwantWarningPanel(term, true, true, R.drawable.server_error, R.string.images_warning_server_error);
            }
        });
    }

    private void searchUrbanDictionaryAsync(final String term) {
        Retrofit retrofit = getRetrofit();
        UrbanDictionaryClient client = retrofit.create(UrbanDictionaryClient.class);
        Call<UrbanDictionaryResults> call = client.getDefinitions(term);

        final View.OnClickListener retryClick = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                searchUrbanDictionaryAsync(term);
            }
        };

        urbanDictionaryTermAdapter = new UrbanDictionaryTermAdapter(getActivity(), ApiConsts.ERROR_CODE_SEARCHING, retryClick);
        setupRecyclerView(urbanDictionaryRecyclerView, R.id.recycler_view_card_urban_dictionary_term, urbanDictionaryTermAdapter, LinearLayoutManager.VERTICAL);

        call.enqueue(new Callback<UrbanDictionaryResults>() {
            @Override
            public void onResponse(Call<UrbanDictionaryResults> call, Response<UrbanDictionaryResults> response) {
                urbanDictionaryTermAdapter = new UrbanDictionaryTermAdapter(getActivity(), response.body(), retryClick);
                setupRecyclerView(urbanDictionaryRecyclerView, R.id.recycler_view_card_urban_dictionary_term, urbanDictionaryTermAdapter, LinearLayoutManager.VERTICAL);
            }

            @Override
            public void onFailure(Call<UrbanDictionaryResults> call, Throwable t) {
                urbanDictionaryTermAdapter = new UrbanDictionaryTermAdapter(getActivity(), ApiConsts.ERROR_CODE_SERVER_ERROR, retryClick);
                setupRecyclerView(urbanDictionaryRecyclerView, R.id.recycler_view_card_urban_dictionary_term, urbanDictionaryTermAdapter, LinearLayoutManager.VERTICAL);
            }
        });
    }

    private void searchWikipediaAsync(final String term) {
        Retrofit retrofit = getRetrofit();
        WikipediaClient client = retrofit.create(WikipediaClient.class);
        Call<WikipediaResults> call = client.getDefinition(term);

        final View.OnClickListener retryClick = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                searchWikipediaAsync(term);
            }
        };

        wikipediaTermAdapter = new WikipediaTermAdapter(getActivity(), ApiConsts.ERROR_CODE_SEARCHING, retryClick);
        setupRecyclerView(wikipediaRecyclerView, R.id.recycler_view_card_wikipedia_term, wikipediaTermAdapter, LinearLayoutManager.VERTICAL);

        call.enqueue(new Callback<WikipediaResults>() {
            @Override
            public void onResponse(Call<WikipediaResults> call, Response<WikipediaResults> response) {
                wikipediaTermAdapter = new WikipediaTermAdapter(getActivity(), response.body(), retryClick);
                setupRecyclerView(wikipediaRecyclerView, R.id.recycler_view_card_wikipedia_term, wikipediaTermAdapter, LinearLayoutManager.VERTICAL);

            }

            @Override
            public void onFailure(Call<WikipediaResults> call, Throwable t) {
                wikipediaTermAdapter = new WikipediaTermAdapter(getActivity(), ApiConsts.ERROR_CODE_SERVER_ERROR, retryClick);
                setupRecyclerView(wikipediaRecyclerView, R.id.recycler_view_card_wikipedia_term, wikipediaTermAdapter, LinearLayoutManager.VERTICAL);

            }
        });
    }

    private void searchMorfixAsync(final String term) {
        Retrofit retrofit = getRetrofit();
        MorfixClient client = retrofit.create(MorfixClient.class);
        Call<MorfixResults> call = client.getDefinitions(term);

        final View.OnClickListener retryClick = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                searchMorfixAsync(term);
            }
        };

        morfixTermAdapter = new MorfixTermAdapter(getActivity(), ApiConsts.ERROR_CODE_SEARCHING, retryClick);
        setupRecyclerView(morfixRecyclerView, R.id.recycler_view_card_morfix_term, morfixTermAdapter, LinearLayoutManager.VERTICAL);

        call.enqueue(new Callback<MorfixResults>() {
            @Override
            public void onResponse(Call<MorfixResults> call, Response<MorfixResults> response) {
                morfixTermAdapter = new MorfixTermAdapter(getActivity(), response.body(), retryClick);
                setupRecyclerView(morfixRecyclerView, R.id.recycler_view_card_morfix_term, morfixTermAdapter, LinearLayoutManager.VERTICAL);

            }

            @Override
            public void onFailure(Call<MorfixResults> call, Throwable t) {
                morfixTermAdapter = new MorfixTermAdapter(getActivity(), ApiConsts.ERROR_CODE_SERVER_ERROR, retryClick);
                setupRecyclerView(morfixRecyclerView, R.id.recycler_view_card_morfix_term, morfixTermAdapter, LinearLayoutManager.VERTICAL);

            }
        });
    }

    private Retrofit getRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(getServerURL())
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build();
    }

    private void setupRecyclerView(RecyclerView recyclerView, int recyclerViewID, RecyclerView.Adapter adapter, int orientation){
        if (recyclerView == null) {
            recyclerView = (RecyclerView) view.findViewById(recyclerViewID);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), orientation, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(false);
        } else {
            recyclerView.swapAdapter(adapter, false);
        }
    }

    private void setQwantWarningPanel(final String term, boolean isWarning, boolean showRetryPanel, int imageResource, int textResource) {
        if (isWarning) {
            containerImagesWarningLayout.setVisibility(View.VISIBLE);
            if (showRetryPanel) {
                containerImagesWarningRetryLayout.setVisibility(View.VISIBLE);
            } else {
                containerImagesWarningRetryLayout.setVisibility(View.GONE);
            }
            containerImagesRecyclerLayout.setVisibility(View.GONE);

            containerImagesWarningImage.setImageResource(imageResource);
            containerImagesWarningText.setText(getResources().getString(textResource));

            containerImagesWarningRetryLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchQwantImagesAsync(term);
                }
            });
        } else {
            containerImagesWarningLayout.setVisibility(View.GONE);
            containerImagesWarningRetryLayout.setVisibility(View.GONE);
            containerImagesRecyclerLayout.setVisibility(View.VISIBLE);
        }
    }

    public LocalPreferences getLocalPreferences() {
        return ((MainActivity) getActivity()).getLocalPreferences();
    }

    public String getServerURL() {
        if (getLocalPreferences().isCustomServer()) {
            return getLocalPreferences().getCustonURLServer();
        }

        return ApiConsts.SERVER_BASE_URL;
    }

    public void safeSetVisivility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }
}
