package com.eviabs.dicts.Adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eviabs.dicts.ApiClients.ApiConsts;
import com.eviabs.dicts.SearchProviders.Results;
import com.eviabs.dicts.R;

import okhttp3.ResponseBody;

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

    private Drawable noTermsFoundImage;
    private String noTermsFoundText;
    private Drawable searchingImage;
    private String searchingText;
    private Drawable errorImage;
    private String errorText;

    private View.OnClickListener retryOnClickListener;
    private int error;

    public class InnerTermViewHolder extends RecyclerView.ViewHolder {

        LinearLayout errorLayout;
        ImageView errorImage;
        TextView errorText;
        LinearLayout retryLayout;
        private LinearLayout definitionContainer;

        protected TermAdapterViewHolder outerTermAdapter;

        protected View view;

        private InnerTermViewHolder(View view) {
            super(view);
            this.errorLayout = view.findViewById(R.id.card_warning);
            this.errorImage = view.findViewById(R.id.card_warning_info_image);
            this.errorText = view.findViewById(R.id.card_warning_info_text);
            this.retryLayout = view.findViewById(R.id.card_warning_retry);
            this.definitionContainer = view.findViewById(R.id.linear_layout_definition);
            this.view = view;
        }
    }

    protected TermAdapter(Context mContext, int error, ResponseBody results, View.OnClickListener retryOnClickListener) {
        this.mContext = mContext;
        this.retryOnClickListener = retryOnClickListener;
        this.error = error;
        this.results = createResultsObject(results);

        // Set images and texts
        this.noTermsFoundImage = mContext.getResources().getDrawable(R.drawable.no_terms_found);
        this.noTermsFoundText = mContext.getResources().getString(R.string.dictionaries_error_no_results);

        this.searchingImage = mContext.getResources().getDrawable(R.drawable.searching_terms);
        this.searchingText =  mContext.getResources().getString(R.string.dictionaries_error_searching);

        this.errorImage = mContext.getResources().getDrawable(R.drawable.server_error);
        this.errorText =  mContext.getResources().getString(R.string.images_warning_server_error);
    }

    public void setNoTermsFoundImage(Drawable noTermsFoundImage) {
        this.noTermsFoundImage = noTermsFoundImage;
    }

    public void setNoTermsFoundText(String noTermsFoundText) {
        this.noTermsFoundText = noTermsFoundText;
    }

    public void setSearchingImage(Drawable searchingImage) {
        this.searchingImage = searchingImage;
    }

    public void setSearchingText(String searchingText) {
        this.searchingText = searchingText;
    }

    public void setErrorImage(Drawable errorImage) {
        this.errorImage = errorImage;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    protected int getError() {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_term, parent, false);
        ((ImageView) itemView.findViewById(R.id.card_dictionary_icon)).setImageDrawable(getIconDrawable());
        ((LinearLayout) itemView.findViewById(R.id.linear_layout_definition)).addView(LayoutInflater.from(parent.getContext()).inflate(getDefinitionLayoutId(), null, false));

        return new InnerTermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TermAdapter.InnerTermViewHolder holder, int position) {

        holder.retryLayout.setOnClickListener(retryOnClickListener);

        // in this part we take care of the warnings
        switch (getError()) {
            case ApiConsts.ERROR_CODE_NO_RESULTS:
                holder.definitionContainer.setVisibility(View.GONE);
                holder.errorImage.setImageDrawable(noTermsFoundImage);
                holder.errorText.setText(noTermsFoundText);
                holder.errorLayout.setVisibility(View.VISIBLE);
                holder.retryLayout.setVisibility(View.GONE);
                break;

            case ApiConsts.ERROR_CODE_SEARCHING:
                holder.definitionContainer.setVisibility(View.GONE);
                holder.errorImage.setImageDrawable(searchingImage);
                holder.errorText.setText(searchingText);
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
                holder.errorImage.setImageDrawable(errorImage);
                holder.errorText.setText(errorText);
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

    protected Drawable getIconDrawable() {
        return null;
    }

    /**
     * This function is called when a term results were found, and should be displayed.
     * Here we set up the layout of the results.
     *
     * @param holder   The holder
     * @param position The term position
     */
    protected abstract void setDefinitionLayout(TermAdapter.InnerTermViewHolder holder, int position);

    protected abstract Results createResultsObject(ResponseBody responseBody);

    protected abstract int getDefinitionLayoutId();
}
