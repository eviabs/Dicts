package com.eviabs.dicts.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eviabs.dicts.ApiClients.ApiConsts;
import com.eviabs.dicts.Dictionaries.Results;
import com.eviabs.dicts.R;

/**
 * An abstract class that resemble a general term adapter.
 * This class takes care of the error handling of search process.
 * It shows the relevant warning when an error occurs, and display the results
 * when available.
 */
public abstract class TermAdapter extends RecyclerView.Adapter<TermAdapter.InnerTermViewHolder> {

    protected Context mContext;
    protected int layout;
    protected Results results;

    private View.OnClickListener retryOnClickListener;
    private int error;

    public class InnerTermViewHolder extends RecyclerView.ViewHolder {

        LinearLayout errorLayout;
        ImageView errorImage;
        TextView errorText;
        LinearLayout retryLayout;
        protected LinearLayout definitionContainer;

        protected TermAdapterViewHolder outerTermAdapter;

        protected View view;

        public InnerTermViewHolder(View view) {
            super(view);
            this.errorLayout = view.findViewById(R.id.card_warning);
            this.errorImage = view.findViewById(R.id.card_warning_info_image);
            this.errorText = view.findViewById(R.id.card_warning_info_text);
            this.retryLayout = view.findViewById(R.id.card_warning_retry);
            this.definitionContainer = view.findViewById(R.id.linear_layout_definition);
            this.view = view;
        }
    }

    public TermAdapter(Context mContext, int layout, Results results, View.OnClickListener retryOnClickListener) {
        this.mContext = mContext;
        this.layout = layout;
        this.retryOnClickListener = retryOnClickListener;
        this.error = ApiConsts.ERROR_CODE_UNINITIALIZED;
        this.results = results;
    }

    public TermAdapter(Context mContext, int error, int layout, View.OnClickListener retryOnClickListener) {
        this(mContext, layout, null, retryOnClickListener);
        this.error = error;
    }

    private int getError() {
        if (error == ApiConsts.ERROR_CODE_UNINITIALIZED) {
            if (results == null) {
                return ApiConsts.ERROR_CODE_UNEXPECTED_ERROR;
            } else {
                return results.getError();
            }
        }
        return error;
    }

    @Override
    public TermAdapter.InnerTermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new InnerTermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TermAdapter.InnerTermViewHolder holder, int position) {

        holder.retryLayout.setOnClickListener(retryOnClickListener);

        // in this part we take care of the warnings
        switch (getError()) {
            case ApiConsts.ERROR_CODE_NO_RESULTS:
                holder.definitionContainer.setVisibility(View.GONE);
                holder.errorImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.no_terms_found));
                holder.errorText.setText(mContext.getResources().getString(R.string.dictionaries_error_no_results));
                holder.errorLayout.setVisibility(View.VISIBLE);
                holder.retryLayout.setVisibility(View.GONE);
                break;

            case ApiConsts.ERROR_CODE_SEARCHING:
                holder.definitionContainer.setVisibility(View.GONE);
                holder.errorImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.searching_terms));
                holder.errorText.setText(mContext.getResources().getString(R.string.dictionaries_error_searching));
                holder.errorLayout.setVisibility(View.VISIBLE);
                holder.retryLayout.setVisibility(View.GONE);
                break;

            case ApiConsts.ERROR_CODE_NO_ERROR:
                holder.errorLayout.setVisibility(View.GONE);
                holder.definitionContainer.setVisibility(View.VISIBLE);
                setDefinitionLayout(holder, position);
                break;

            default:
                holder.definitionContainer.setVisibility(View.GONE);
                holder.errorImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.server_error));
                holder.errorText.setText(mContext.getResources().getString(R.string.images_warning_server_error));
                holder.errorLayout.setVisibility(View.VISIBLE);
                holder.retryLayout.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        if (results == null || results.getCount() == 0) {
            return 1;
        }
        return results.getCount();
    }

    /**
     * This function is called when a term results were found, and should be displayed.
     * Here we set up the layout of the results.
     *
     * @param holder   The holder
     * @param position The term position
     */
    protected abstract void setDefinitionLayout(TermAdapter.InnerTermViewHolder holder, int position);

}
