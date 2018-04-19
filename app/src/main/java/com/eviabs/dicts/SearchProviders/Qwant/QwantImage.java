package com.eviabs.dicts.SearchProviders.Qwant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QwantImage {
    @SerializedName("media")
    @Expose
    private String media;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public QwantImage(String media, String thumbnail) {
        this.media = media;
        this.thumbnail = thumbnail;
    }

    public String getMedia() {
        return this.media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}

