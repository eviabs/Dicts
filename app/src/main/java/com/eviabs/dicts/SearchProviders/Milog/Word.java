
package com.eviabs.dicts.SearchProviders.Milog;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Word implements Serializable
{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("definitions")
    @Expose
    private List<String> definitions = null;
    private final static long serialVersionUID = 2750101320495759102L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Word() {
    }

    /**
     * 
     * @param title
     * @param definitions
     */
    public Word(String title, List<String> definitions) {
        super();
        this.title = title;
        this.definitions = definitions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

}
