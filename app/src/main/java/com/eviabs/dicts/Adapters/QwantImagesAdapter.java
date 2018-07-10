package com.eviabs.dicts.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.eviabs.dicts.ApiClients.ApiConsts;
import com.eviabs.dicts.SearchProviders.Qwant.QwantImage;
import com.eviabs.dicts.SearchProviders.Qwant.QwantImageResults;
import com.eviabs.dicts.SearchProviders.Results;
import com.eviabs.dicts.R;
import com.eviabs.dicts.Utils.TextDrawable;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.IOException;

import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifDrawable;

public class QwantImagesAdapter extends TermAdapter {

    public class MyViewHolder extends TermAdapterViewHolder {

        public ImageView image;

        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image_view_list_item_image);

        }
    }

    public QwantImagesAdapter(Context mContext, int error, ResponseBody responseBody, View.OnClickListener retryOnClickListener) {
        super(mContext, error, responseBody, retryOnClickListener);
        setNoTermsFoundImage(mContext.getResources().getDrawable(R.drawable.no_images_found));
        setNoTermsFoundText(mContext.getResources().getString(R.string.images_warning_no_images));

        setSearchingImage(mContext.getResources().getDrawable(R.drawable.searching_images));
        setSearchingText(mContext.getResources().getString(R.string.images_warning_searching));
    }

    private TextDrawable getTextDrawable(View view, int stringResource) {
        TextDrawable loadingDrawable = new TextDrawable(view.getContext());
        loadingDrawable.setText(view.getContext().getResources().getString(stringResource));
        loadingDrawable.setTextAlign(Layout.Alignment.ALIGN_CENTER);
        loadingDrawable.setTextColor(Color.WHITE);
        loadingDrawable.setTextSize(14);

        return loadingDrawable;
    }

    @Override
    public InnerTermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InnerTermViewHolder innerTermViewHolder = super.onCreateViewHolder(parent, viewType);
        innerTermViewHolder.outerTermAdapter = new MyViewHolder(innerTermViewHolder.view);
        return innerTermViewHolder;

    }

    @Override
    protected void setDefinitionLayout(InnerTermViewHolder oldHolder, final int position) {

        // Change the image width
        ViewGroup.LayoutParams paramsLayoutCardTermContainer = oldHolder.view.findViewById(R.id.card_term_container).getLayoutParams();
        paramsLayoutCardTermContainer.width = ViewGroup.LayoutParams.WRAP_CONTENT;

        final QwantImageResults term = ((QwantImageResults) results);
        final MyViewHolder holder = ((MyViewHolder) oldHolder.outerTermAdapter);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open full screen image

                // create error drawable to show when image cannot be loaded
                TextDrawable errorDrawable = getTextDrawable(view, R.string.images_warning_loading_error);
                TextDrawable loadingDrawable = getTextDrawable(view, R.string.images_warning_loading);

                // create the custom hierarchyBuilder which sets a loading + error images
                GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(mContext.getResources())
                        .setFailureImage(errorDrawable)
                        .setPlaceholderImage(loadingDrawable)
                        .setProgressBarImage(new ProgressBarDrawable());

                // build the full screen images screen
                new ImageViewer.Builder<>(mContext, term.getImages())
                        .setFormatter(new ImageViewer.Formatter<QwantImage>() {
                            @Override
                            public String format(QwantImage customImage) {
                                return customImage.getMedia();
                            }
                        })
                        .setStartPosition(position)
                        .setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                        .show();
            }
        });

        // Start animation - rotate the spinner
        RotateAnimation rotate = new RotateAnimation(0, 4320 , Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(15000);
        rotate.setInterpolator(new LinearInterpolator());
        holder.image.startAnimation(rotate);

        // load the image
        Picasso.get()
                .load(term.getImages().get(position).getThumbnail())
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.ic_suggestion)
                .fit()
                .into(holder.image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.image.setAnimation(null);
                    }

                    @Override
                    public void onError(Exception ex) {

                    }
                });


//      GifDrawable seems to block the mui thread, so we will comment the code until it is fixed.
//        try {
//            // smooth spinner
//            Picasso.get()
//                    .load(term.getImages().get(position).getThumbnail())
//                    .placeholder(new GifDrawable(mContext.getResources(), R.drawable.small_spinner))
//                    .error(R.drawable.ic_suggestion)
//                    .fit()
//                    .into(holder.image);
//            // if failed, load the ugly one :(
//        } catch (IOException ex) {
//            Picasso.get()
//                    .load(term.getImages().get(position).getThumbnail())
//                    .placeholder(R.drawable.image_loading_animation)
//                    .error(R.drawable.ic_suggestion)
//                    .fit()
//                    .into(holder.image);
//        }
    }

    @Override
    protected Results createResultsObject(ResponseBody responseBody) {
        if (responseBody != null) {
            try {
                return new Gson().fromJson(responseBody.string(), QwantImageResults.class);
            } catch (IOException ex) {
                // do nothing
            }
        }
        return null;
    }

    @Override
    protected int getDefinitionLayoutId() {
        return R.layout.list_item_image;
    }

    @Override
    public void onBindViewHolder(TermAdapter.InnerTermViewHolder holder, int position) {

        changeImagesLayout(holder);
        super.onBindViewHolder(holder, position);
    }

    /**
     * We use this function to programmatically change the images recyclerview layout.
     * @param holder the holder
     */
    private void changeImagesLayout(InnerTermViewHolder holder) {
        holder.view.findViewById(R.id.card_warning).setPadding(0, 0, 0, 0);
        holder.view.findViewById(R.id.card_warning_info).setPadding(0, 0, 0, 0);


        CardView cardView = (CardView) holder.view.findViewById(R.id.card_term);

        cardView.setBackgroundColor(mContext.getResources().getColor(R.color.colorTransparent));
        cardView.setPadding(0, 0, 0, 0);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
        params.setMarginStart((int) (mContext.getResources().getDimension(R.dimen.card_images_gap)));
        params.setMarginEnd((int) (mContext.getResources().getDimension(R.dimen.card_images_gap)));


        ViewGroup.LayoutParams paramsLayoutCardTermContainer = holder.view.findViewById(R.id.card_term_container).getLayoutParams();
        paramsLayoutCardTermContainer.width = ViewGroup.LayoutParams.MATCH_PARENT;


        ViewGroup.LayoutParams paramsLayout2 = (ViewGroup.LayoutParams) holder.errorImage.getLayoutParams();
        paramsLayout2.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsLayout2.height = (int) (mContext.getResources().getDimension(R.dimen.image_small_height));
    }
}
