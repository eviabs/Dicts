
package com.eviabs.dicts.Dictionaries.Morfix;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Inflection {

    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("Title")
    @Expose
    private String title;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
