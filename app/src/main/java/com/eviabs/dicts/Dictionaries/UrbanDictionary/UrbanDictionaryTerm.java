package com.eviabs.dicts.Dictionaries.UrbanDictionary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UrbanDictionaryTerm {
    @SerializedName("definition")
    @Expose
    private String definition;

    @SerializedName("word")
    @Expose
    private String word;

    @SerializedName("thumbs_up")
    @Expose
    private long thumbs_up;

    @SerializedName("thumbs_down")
    @Expose
    private long thumbs_down;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("example")
    @Expose
    private String example;

    public UrbanDictionaryTerm(String definition, String word, long thumbs_up, long thumbs_down, String author, String example) {
        this.definition = definition;
        this.word = word;
        this.thumbs_up = thumbs_up;
        this.thumbs_down = thumbs_down;
        this.author = author;
        this.example = example;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getThumbs_up() {
        return thumbs_up;
    }

    public void setThumbs_up(long thumbs_up) {
        this.thumbs_up = thumbs_up;
    }

    public long getThumbs_down() {
        return thumbs_down;
    }

    public void setThumbs_down(long thumbs_down) {
        this.thumbs_down = thumbs_down;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
