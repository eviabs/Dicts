package com.eviabs.dicts.Dictionaries.Qwant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class QwantImageResults {
    @SerializedName("error")
    @Expose
    private int error;

    @SerializedName("images")
    @Expose
    private List<QwantImage> images;

    public QwantImageResults(int error, List<QwantImage> images) {
        this.error = error;
        this.images = images;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<QwantImage> getImages() {
        return images;
    }

    public void setImages(List<QwantImage> images) {
        this.images = images;
    }
}
