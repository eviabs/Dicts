package com.eviabs.dicts.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eviabs.dicts.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchResultsFragment extends Fragment {

    View view = null;

    public SearchResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_search_results, container, false);
        return view;
    }

    public void search(String query) {
        TextView txt = view.findViewById(R.id.frag_txt);
        txt.setText(query);
    }
}
