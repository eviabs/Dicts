package com.eviabs.dicts.SearchProviders.Qwant;

import com.eviabs.dicts.SearchProviders.Results;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QwantImageResults extends Results {
    @SerializedName("images")
    @Expose
    private List<QwantImage> images;

    public QwantImageResults(int error) {
        super(error);
    }

    public List<QwantImage> getImages() {
        return images;
    }

    public void setImages(List<QwantImage> images) {
        this.images = images;
    }

    @Override
    public int getCount() {
        if ( this.images == null) {
            return 0;
        }

        return this.images.size();
    }
}
