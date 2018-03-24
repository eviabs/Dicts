package com.eviabs.dicts.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eviabs.dicts.Activities.MainActivity;
import com.eviabs.dicts.Adapters.MorfixTermAdapter;
import com.eviabs.dicts.Adapters.QwantImagesAdapter;
import com.eviabs.dicts.Adapters.UrbanDictionaryTermAdapter;
import com.eviabs.dicts.Adapters.WikipediaTermAdapter;
import com.eviabs.dicts.ApiClients.MorfixClient;
import com.eviabs.dicts.ApiClients.QwantImageClient;
import com.eviabs.dicts.ApiClients.UrbanDictionaryClient;
import com.eviabs.dicts.ApiClients.WikipediaClient;
import com.eviabs.dicts.Dictionaries.Morfix.MorfixResults;
import com.eviabs.dicts.Dictionaries.Qwant.QwantImageResults;
import com.eviabs.dicts.Dictionaries.UrbanDictionary.UrbanDictionaryResults;
import com.eviabs.dicts.Dictionaries.Wikipedia.WikipediaResults;
import com.eviabs.dicts.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import static com.eviabs.dicts.ApiClients.ApiConsts.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchResultsFragment extends Fragment {

    private View view = null;

    private QwantImagesAdapter qwantImagesAdapter = null;
    private RecyclerView qwantRecyclerView = null;

    private UrbanDictionaryTermAdapter urbanDictionaryTermAdapter = null;
    private RecyclerView urbanDictionaryRecyclerView = null;

    private WikipediaTermAdapter wikipediaTermAdapter = null;
    private RecyclerView wikipediaRecyclerView = null;

    private MorfixTermAdapter morfixTermAdapter = null;
    private RecyclerView morfixRecyclerView = null;

    private RelativeLayout containerImagesWarningLayout = null;
    private ImageView containerImagesWarningImage = null;
    private TextView containerImagesWarningText = null;


    private LinearLayout containerDictionariesWarningLayout = null;
    private ImageView containerDictionariesWarningImage = null;
    private TextView containerDictionariesWarningText = null;

    public SearchResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_results, container, false);

        containerImagesWarningLayout = view.findViewById(R.id.container_images_warning);
        containerImagesWarningImage = view.findViewById(R.id.container_images_warning_image);
        containerImagesWarningText = view.findViewById(R.id.container_images_warning_text);

        containerDictionariesWarningLayout = view.findViewById(R.id.container_dictionaries_warning);
        containerDictionariesWarningImage = view.findViewById(R.id.container_dictionaries_warning_image);
        containerDictionariesWarningText = view.findViewById(R.id.container_dictionaries_text);
        return view;
    }

    public void search(String query) {

        searchQwantImagesAsync(query, 10);

        containerDictionariesWarningLayout.setVisibility(View.GONE);
        containerDictionariesWarningImage.setVisibility(View.GONE);
        containerDictionariesWarningText.setVisibility(View.GONE);

//        searchUrbanDictionaryAsync(query);
//        searchWikipediaAsync(query);
        morfixWikipediaAsync(query);
    }

    private void searchQwantImagesAsync(String term, int count) {
        Retrofit retrofit = getRetrofit();
        QwantImageClient client = retrofit.create(QwantImageClient.class);
        Call<QwantImageResults> call = client.getImages(term, count);

        containerImagesWarningLayout.setVisibility(View.VISIBLE);
        containerImagesWarningImage.setVisibility(View.VISIBLE);
        containerImagesWarningText.setVisibility(View.VISIBLE);

        if (qwantRecyclerView != null) {
            qwantRecyclerView.setVisibility(View.GONE);
        }

        containerImagesWarningImage.setImageResource(R.drawable.searching_images);
        containerImagesWarningText.setText(getResources().getString(R.string.images_warning_searching));

        call.enqueue(new Callback<QwantImageResults>() {
            @Override
            public void onResponse(Call<QwantImageResults> call, Response<QwantImageResults> response) {
                qwantImagesAdapter = new QwantImagesAdapter(response.body());
                setupRecyclerView(qwantRecyclerView, R.id.recycler_view_list_images, qwantImagesAdapter, LinearLayoutManager.HORIZONTAL);

                switch (response.body().getError()){
                    case ERROR_CODE_NO_ERROR:
                        containerImagesWarningLayout.setVisibility(View.GONE);
                        containerImagesWarningImage.setVisibility(View.GONE);
                        containerImagesWarningText.setVisibility(View.GONE);

                        if (qwantRecyclerView != null) {
                            qwantRecyclerView.setVisibility(View.VISIBLE);
                        }
                        break;

                    case ERROR_CODE_NO_RESULTS:
                        containerImagesWarningImage.setImageResource(R.drawable.no_images_found);
                        containerImagesWarningText.setText(getResources().getString(R.string.images_warning_no_images));

                        if (qwantRecyclerView != null) {
                            qwantRecyclerView.setVisibility(View.GONE);
                        }
                        break;

                    default:
                        break;
                }

            }

            @Override
            public void onFailure(Call<QwantImageResults> call, Throwable t) {
                ((MainActivity) getActivity()).showToast("error");
                //
            }
        });
    }

    private void searchUrbanDictionaryAsync(String term) {
        Retrofit retrofit = getRetrofit();
        UrbanDictionaryClient client = retrofit.create(UrbanDictionaryClient.class);
        Call<UrbanDictionaryResults> call = client.getDefinitions(term);

        call.enqueue(new Callback<UrbanDictionaryResults>() {
            @Override
            public void onResponse(Call<UrbanDictionaryResults> call, Response<UrbanDictionaryResults> response) {
                urbanDictionaryTermAdapter = new UrbanDictionaryTermAdapter(response.body());
                setupRecyclerView(urbanDictionaryRecyclerView, R.id.recycler_view_card_urban_dictionary_term, urbanDictionaryTermAdapter, LinearLayoutManager.VERTICAL);

            }

            @Override
            public void onFailure(Call<UrbanDictionaryResults> call, Throwable t) {
                ((MainActivity) getActivity()).showToast("error");
                //
            }
        });
    }

    private void searchWikipediaAsync(String term) {
        Retrofit retrofit = getRetrofit();
        WikipediaClient client = retrofit.create(WikipediaClient.class);
        Call<WikipediaResults> call = client.getDefinition(term);

        call.enqueue(new Callback<WikipediaResults>() {
            @Override
            public void onResponse(Call<WikipediaResults> call, Response<WikipediaResults> response) {
                wikipediaTermAdapter = new WikipediaTermAdapter(response.body());
                setupRecyclerView(wikipediaRecyclerView, R.id.recycler_view_card_wikipedia_term, wikipediaTermAdapter, LinearLayoutManager.VERTICAL);

            }

            @Override
            public void onFailure(Call<WikipediaResults> call, Throwable t) {
                ((MainActivity) getActivity()).showToast("error");
                //
            }
        });
    }

    private void morfixWikipediaAsync(String term) {
        Retrofit retrofit = getRetrofit();
        MorfixClient client = retrofit.create(MorfixClient.class);
        Call<MorfixResults> call = client.getDefinitions(term);

        call.enqueue(new Callback<MorfixResults>() {
            @Override
            public void onResponse(Call<MorfixResults> call, Response<MorfixResults> response) {
                morfixTermAdapter = new MorfixTermAdapter(response.body());
                setupRecyclerView(morfixRecyclerView, R.id.recycler_view_card_morfix_term, morfixTermAdapter, LinearLayoutManager.VERTICAL);

            }

            @Override
            public void onFailure(Call<MorfixResults> call, Throwable t) {
                ((MainActivity) getActivity()).showToast("error");
                //
            }
        });
    }

    private Retrofit getRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://web-dicts.herokuapp.com/")
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
            qwantRecyclerView.swapAdapter(qwantImagesAdapter, false);
        }
    }
}
