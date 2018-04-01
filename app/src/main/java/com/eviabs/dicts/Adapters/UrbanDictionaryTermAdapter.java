package com.eviabs.dicts.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eviabs.dicts.Activities.MainActivity;
import com.eviabs.dicts.ApiClients.ApiConsts;
import com.eviabs.dicts.Dictionaries.Results;
import com.eviabs.dicts.Dictionaries.UrbanDictionary.UrbanDictionaryResults;
import com.eviabs.dicts.Dictionaries.UrbanDictionary.UrbanDictionaryTerm;
import com.eviabs.dicts.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

public class UrbanDictionaryTermAdapter extends TermAdapter {

    public class MyViewHolder extends TermAdapterViewHolder {

        private TextView word;
        private ExpandableTextView definition;
        private ExpandableTextView example;
        private TextView author;
        private TextView up;
        private TextView down;


        private MyViewHolder(View view) {
            super(view);
            word = (TextView) view.findViewById(R.id.textViewUrbanWord);
            definition = (ExpandableTextView) view.findViewById(R.id.textViewUrbanDefinition);
            example = (ExpandableTextView ) view.findViewById(R.id.textViewUrbanExample);
            author = (TextView) view.findViewById(R.id.textViewUrbanAuthor);
            up = (TextView) view.findViewById(R.id.textViewUrbanThumbsUp);
            down = (TextView) view.findViewById(R.id.textViewUrbanThumbsDown);

        }
    }

    public UrbanDictionaryTermAdapter(Context mContext, Results urbanDictionaryResults, View.OnClickListener retryOnClickListener) {
        super(mContext, R.layout.card_urban_dictionary_term, urbanDictionaryResults, retryOnClickListener);
    }

    public UrbanDictionaryTermAdapter(Context mContext, int error, View.OnClickListener retryOnClickListener) {
        super(mContext, error, R.layout.card_urban_dictionary_term, retryOnClickListener);
    }

    @Override
    public InnerTermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InnerTermViewHolder innerTermViewHolder = super.onCreateViewHolder(parent, viewType);
        innerTermViewHolder.outerTermAdapter = new MyViewHolder(innerTermViewHolder.view);
        return innerTermViewHolder;

    }

    @Override
    public void setDefinitionLayout(InnerTermViewHolder oldHolder, int position) {

        UrbanDictionaryTerm term = ((UrbanDictionaryResults) results).getList().get(position);
        MyViewHolder holder = ((MyViewHolder)oldHolder.outerTermAdapter);

        holder.word.setText(term.getWord());
        holder.definition.setText(term.getDefinition());
        holder.example.setText(term.getExample());
        holder.author.setText(term.getAuthor());
        holder.up.setText(String.valueOf(term.getThumbs_up()));
        holder.down.setText(String.valueOf(term.getThumbs_down()));
    }
}
