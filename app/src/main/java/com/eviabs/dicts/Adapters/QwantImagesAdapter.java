package com.eviabs.dicts.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eviabs.dicts.Dictionaries.Qwant.QwantImage;
import com.eviabs.dicts.Dictionaries.Qwant.QwantImageResults;
import com.eviabs.dicts.R;
import com.eviabs.dicts.Utils.TextDrawable;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;

public class QwantImagesAdapter extends RecyclerView.Adapter<QwantImagesAdapter.MyViewHolder> {

    private List<QwantImage> imagesList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image_view_list_item_image);
        }
    }

    public QwantImagesAdapter(Context context, QwantImageResults qwantImageResults) {
        this.context = context;
        this.imagesList = qwantImageResults.getImages();

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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_image, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open full screen image
                // create error drawable to show when image cannot be loaded
                TextDrawable errorDrawable = getTextDrawable(view, R.string.images_warning_loading_error);
                TextDrawable loadingDrawable = getTextDrawable(view, R.string.images_warning_loading);

                // create the custom hierarchyBuilder which sets a loading + error images
                GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(view.getContext().getResources())
                        .setFailureImage(errorDrawable)
                        .setPlaceholderImage(loadingDrawable)
                        .setProgressBarImage(new ProgressBarDrawable());

                // build the full screen images screen
                new ImageViewer.Builder<>(view.getContext(), imagesList)
                        .setFormatter(new ImageViewer.Formatter<QwantImage>() {
                            @Override
                            public String format(QwantImage customImage) {
                                return customImage.getMedia();
                            }
                        })
                        .setStartPosition(holder.getAdapterPosition())
                        .setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                        .show();
            }
        });

        try {
            // smooth spinner
            Picasso.get()
                    .load(imagesList.get(position).getThumbnail())
                    .placeholder(new GifDrawable(context.getResources(), R.drawable.small_spinner))
                    .error(R.drawable.ic_suggestion)
                    .fit()
                    .into(holder.image);
            // if failed, load the ugly one :(
        } catch (IOException ex) {
            Picasso.get()
                    .load(imagesList.get(position).getThumbnail())
                    .placeholder(R.drawable.image_loading_animation)
                    .error(R.drawable.ic_suggestion)
                    .fit()
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        if (imagesList == null) {
            return 0;
        }
        return imagesList.size();
    }
}