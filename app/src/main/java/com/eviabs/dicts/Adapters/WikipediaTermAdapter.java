package com.eviabs.dicts.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eviabs.dicts.SearchProviders.Results;
import com.eviabs.dicts.SearchProviders.Wikipedia.WikipediaResults;
import com.eviabs.dicts.R;
import com.google.gson.Gson;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.IOException;

import okhttp3.ResponseBody;

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
    public WikipediaTermAdapter(Context mContext, int error, ResponseBody responseBody, View.OnClickListener retryOnClickListener) {
        super(mContext, error, responseBody, retryOnClickListener);
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

    @Override
    protected Results createResultsObject(ResponseBody responseBody) {
        if (responseBody != null) {
            try {
                return new Gson().fromJson(responseBody.string(), WikipediaResults.class);
            } catch (IOException ex) {
                // do nothing
            }
        }
        return null;
    }

    @Override
    protected int getDefinitionLayoutId() {
        return R.layout.definition_wikipedia;
    }

    @Override
    protected Drawable getIconDrawable() {
        return mContext.getResources().getDrawable(R.drawable.wikipedia_480);
    }
}
