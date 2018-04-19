package com.eviabs.dicts.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eviabs.dicts.SearchProviders.Morfix.MorfixResults;
import com.eviabs.dicts.SearchProviders.Morfix.MorfixUtils;
import com.eviabs.dicts.SearchProviders.Morfix.Word;
import com.eviabs.dicts.SearchProviders.Results;
import com.eviabs.dicts.R;
import com.google.gson.Gson;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.IOException;

import okhttp3.ResponseBody;

public class MorfixTermAdapter extends TermAdapter {

    public class MyViewHolder extends TermAdapterViewHolder {

        public CardView card;
        public TextView word;
        public TextView partOfSpeech;
        public TextView inflections;

        public TextView definition;

        public LinearLayout exampleLayout;
        public ExpandableTextView example;

        public LinearLayout synonymsLayout;
        public TextView synonyms;

        public MyViewHolder(View view) {
            super(view);

            card = view.findViewById(R.id.card_term);

            word = view.findViewById(R.id.textViewMorfixWord);
            partOfSpeech = view.findViewById(R.id.textViewMorfixPartOfSpeech);
            inflections = view.findViewById(R.id.textViewMorfixInflections);

            definition = view.findViewById(R.id.textViewMorfixDefinition);

            exampleLayout = view.findViewById(R.id.containerMorfixExample);
            example = view.findViewById(R.id.textViewMorfixExample);

            synonymsLayout = view.findViewById(R.id.containerMorfixSynonyms);
            synonyms = view.findViewById(R.id.textViewMorfixSynonyms);

        }
    }

    public MorfixTermAdapter(Context mContext, int error, ResponseBody responseBody, View.OnClickListener retryOnClickListener) {
        super(mContext, error, responseBody, retryOnClickListener);
    }

    @Override
    public InnerTermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InnerTermViewHolder innerTermViewHolder = super.onCreateViewHolder(parent, viewType);
        innerTermViewHolder.outerTermAdapter = new MyViewHolder(innerTermViewHolder.view);
        return innerTermViewHolder;

    }

    @Override
    protected void setDefinitionLayout(InnerTermViewHolder oldHolder, int position) {

        MorfixResults term = ((MorfixResults) results);
        MyViewHolder holder = ((MyViewHolder) oldHolder.outerTermAdapter);

        holder.card.setLayoutDirection(MorfixUtils.getDirection(term));
        Word currentWord = term.getWords().get(position);
        holder.word.setText(currentWord.getInputLanguageMeanings().get(0).get(0).getDisplayText());
        holder.partOfSpeech.setText(currentWord.getPartOfSpeech());
        holder.inflections.setText(MorfixUtils.inflectionsToString(currentWord.getInflections()));
        holder.definition.setText(currentWord.getOutputLanguageMeaningsString());

        holder.exampleLayout.setVisibility(View.GONE);
        String examples = MorfixUtils.examplesToString(currentWord.getSampleSentences());
        if (!examples.equals("")) {
            holder.exampleLayout.setVisibility(View.VISIBLE);
            holder.example.setText(Html.fromHtml(examples));
        }

        holder.synonymsLayout.setVisibility(View.GONE);
        String synonyms = MorfixUtils.synonymsToString(currentWord.getSynonymsList());
        if (!synonyms.equals("")) {
            holder.synonymsLayout.setVisibility(View.VISIBLE);
            holder.synonyms.setText(synonyms);
        }


    }

    @Override
    protected Results createResultsObject(ResponseBody responseBody) {
        if (responseBody != null) {
            try {
                return new Gson().fromJson(responseBody.string(), MorfixResults.class);
            } catch (IOException ex) {
                // do nothing
            }
        }
        return null;
    }

    @Override
    protected int getDefinitionLayoutId() {
        return R.layout.definition_morfix;
    }

    @Override
    protected Drawable getIconDrawable() {
        return mContext.getResources().getDrawable(R.drawable.morfix_300);
    }
}
