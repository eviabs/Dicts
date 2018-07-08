package com.eviabs.dicts.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eviabs.dicts.R;
import com.eviabs.dicts.SearchProviders.Milog.MilogResults;
import com.eviabs.dicts.SearchProviders.Results;
import com.google.gson.Gson;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.IOException;

import okhttp3.ResponseBody;

public class MilogTermAdapter extends TermAdapter {

    public class MyViewHolder extends TermAdapterViewHolder {
        public TextView title;
        public ExpandableTextView definition;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textViewMilogTitle);
            definition = (ExpandableTextView) view.findViewById(R.id.textViewMilogDefinition);

        }
    }

    public MilogTermAdapter(Context mContext, int error, ResponseBody responseBody, View.OnClickListener retryOnClickListener) {
        super(mContext, error, responseBody, retryOnClickListener);
    }

    @Override
    public InnerTermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InnerTermViewHolder innerTermViewHolder = super.onCreateViewHolder(parent, viewType);
        innerTermViewHolder.outerTermAdapter = new MyViewHolder(innerTermViewHolder.view);
        return innerTermViewHolder;

    }

    @Override
    protected void setDefinitionLayout(InnerTermViewHolder holder, int position) {
        MilogResults term = ((MilogResults) results);
        MyViewHolder myViewHolder = ((MyViewHolder)holder.outerTermAdapter);

        myViewHolder.title.setText(Html.fromHtml(term.getWords().get(position).getTitle()));

        StringBuilder concatDefinitions = new StringBuilder();

        for (String def: term.getWords().get(position).getDefinitions()) {
            concatDefinitions.append(def);
        }
        myViewHolder.definition.setText(Html.fromHtml(concatDefinitions.toString()));
    }

    @Override
    protected Results createResultsObject(ResponseBody responseBody) {
        if (responseBody != null) {
            try {
                return new Gson().fromJson(responseBody.string(), MilogResults.class);
            } catch (IOException ex) {
                // do nothing
            }
        }
        return null;
    }

    @Override
    protected int getDefinitionLayoutId() {
        return R.layout.definition_milog;
    }

    @Override
    protected Drawable getIconDrawable() {
        return mContext.getResources().getDrawable(R.drawable.milog_180);
    }
}
