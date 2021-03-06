package com.eviabs.dicts.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eviabs.dicts.SearchProviders.Results;
import com.eviabs.dicts.SearchProviders.UrbanDictionary.UrbanDictionaryResults;
import com.eviabs.dicts.SearchProviders.UrbanDictionary.UrbanDictionaryTerm;
import com.eviabs.dicts.R;
import com.eviabs.dicts.Utils.SoundPlayer;
import com.google.gson.Gson;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;

public class UrbanDictionaryTermAdapter extends TermAdapter {

    public class MyViewHolder extends TermAdapterViewHolder {

        private TextView word;
        private ExpandableTextView definition;
        private ExpandableTextView example;
        private TextView author;
        private TextView up;
        private TextView down;
        public SoundPlayer soundPlayer = null;
        public LinearLayout soundLayout;


        private MyViewHolder(View view) {
            super(view);
            word = (TextView) view.findViewById(R.id.textViewUrbanWord);
            definition = (ExpandableTextView) view.findViewById(R.id.textViewUrbanDefinition);
            example = (ExpandableTextView ) view.findViewById(R.id.textViewUrbanExample);
            author = (TextView) view.findViewById(R.id.textViewUrbanAuthor);
            up = (TextView) view.findViewById(R.id.textViewUrbanThumbsUp);
            down = (TextView) view.findViewById(R.id.textViewUrbanThumbsDown);
            soundLayout = view.findViewById(R.id.sound_layout);

        }
    }

    public UrbanDictionaryTermAdapter(Context mContext, int error,  ResponseBody responseBody, View.OnClickListener retryOnClickListener) {
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

        UrbanDictionaryTerm term = ((UrbanDictionaryResults) results).getList().get(position);
        List<String> sounds = ((UrbanDictionaryResults) results).getSounds();
        MyViewHolder holder = ((MyViewHolder)oldHolder.outerTermAdapter);

        holder.word.setText(term.getWord());

        holder.definition.setText(term.getDefinition());
//        ((TextView)holder.definition.findViewById(R.id.expandable_text)).setTextIsSelectable(false);

        holder.example.setText(term.getExample());
//        ((TextView)holder.example.findViewById(R.id.expandable_text)).setTextIsSelectable(false);
//        holder.example.setOnExpandStateChangeListener(new ExpandableTextView.OnExpandStateChangeListener() {
//            @Override
//            public void onExpandStateChanged(TextView textView, boolean isExpanded) {
//                if (isExpanded) {
//                    textView.setTextIsSelectable(true);
//                } else {
//                    textView.setTextIsSelectable(false);
//                }
//            }
//        });

        holder.author.setText(term.getAuthor());
        holder.up.setText(String.valueOf(term.getThumbs_up()));
        holder.down.setText(String.valueOf(term.getThumbs_down()));

        if (sounds != null && !sounds.isEmpty()) {
            holder.soundLayout.setVisibility(View.VISIBLE);
            holder.soundLayout.setVisibility(View.VISIBLE);
            if (holder.soundPlayer != null) {
                holder.soundPlayer.release();
            }
            holder.soundPlayer = new SoundPlayer(mContext, Uri.parse(sounds.get(0)), holder.soundLayout);
        } else {
            holder.soundLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected Results createResultsObject(ResponseBody responseBody) {
        if (responseBody != null) {
            try {
                return new Gson().fromJson(responseBody.string(), UrbanDictionaryResults.class);
            } catch (IOException ex) {
                // do nothing
            }
        }
        return null;
    }

    @Override
    protected int getDefinitionLayoutId() {
        return R.layout.definition_urban_dictionary;
    }

    @Override
    protected Drawable getIconDrawable() {
        return mContext.getResources().getDrawable(R.drawable.urban_512);
    }
}
