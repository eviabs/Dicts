package com.eviabs.dicts.SearchProviders.Wikipedia;

import com.eviabs.dicts.SearchProviders.Results;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WikipediaResults extends Results {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("extract")
    @Expose
    private String extract;

    public WikipediaResults(int error, String title, String extract) {
        super(error);
        this.title = title;
        this.extract = extract;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    @Override
    public int getCount() {
        return (getTitle().equals("")) ? 0 : 1;
    }
}
