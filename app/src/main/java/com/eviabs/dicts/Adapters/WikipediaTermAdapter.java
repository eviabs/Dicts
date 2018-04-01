package com.eviabs.dicts.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eviabs.dicts.Dictionaries.Results;
import com.eviabs.dicts.Dictionaries.Wikipedia.WikipediaResults;
import com.eviabs.dicts.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class WikipediaTermAdapter extends TermAdapter {

    public class MyViewHolder extends TermAdapterViewHolder {
        public TextView title;
        public ExpandableTextView extract;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textViewWikipediaTitle);
            extract = (ExpandableTextView) view.findViewById(R.id.textViewWikipediaExtract);

        }
    }
    public WikipediaTermAdapter(Context mContext, Results wikipediaResults, View.OnClickListener retryOnClickListener) {
        super(mContext, R.layout.card_wikipedia_term, wikipediaResults, retryOnClickListener);
    }

    public WikipediaTermAdapter(Context mContext, int error, View.OnClickListener retryOnClickListener) {
        super(mContext, error, R.layout.card_wikipedia_term, retryOnClickListener);
    }

    @Override
    public InnerTermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InnerTermViewHolder innerTermViewHolder = super.onCreateViewHolder(parent, viewType);
        innerTermViewHolder.outerTermAdapter = new MyViewHolder(innerTermViewHolder.view);
        return innerTermViewHolder;

    }

    @Override
    public void setDefinitionLayout(InnerTermViewHolder oldHolder, int position) {

        WikipediaResults term = ((WikipediaResults) results);
        MyViewHolder holder = ((MyViewHolder)oldHolder.outerTermAdapter);

        holder.title.setText(term.getTitle());
        holder.extract.setText(term.getExtract());
    }
}
