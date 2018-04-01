package com.eviabs.dicts.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eviabs.dicts.Dictionaries.Morfix.MorfixResults;
import com.eviabs.dicts.Dictionaries.Morfix.MorfixUtils;
import com.eviabs.dicts.Dictionaries.Morfix.Word;
import com.eviabs.dicts.Dictionaries.Results;
import com.eviabs.dicts.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

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

    public MorfixTermAdapter(Context mContext, Results morfixResults, View.OnClickListener retryOnClickListener) {
        super(mContext, R.layout.card_morfix_term, morfixResults, retryOnClickListener);
    }

    public MorfixTermAdapter(Context mContext, int error, View.OnClickListener retryOnClickListener) {
        super(mContext, error, R.layout.card_morfix_term, retryOnClickListener);
    }


    @Override
    public InnerTermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InnerTermViewHolder innerTermViewHolder = super.onCreateViewHolder(parent, viewType);
        innerTermViewHolder.outerTermAdapter = new MyViewHolder(innerTermViewHolder.view);
        return innerTermViewHolder;

    }

    @Override
    public void setDefinitionLayout(InnerTermViewHolder oldHolder, int position) {

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
}
