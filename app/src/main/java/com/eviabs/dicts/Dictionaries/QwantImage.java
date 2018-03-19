package com.eviabs.dicts.Dictionaries;

public class QwantImage {
    private String media;
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

