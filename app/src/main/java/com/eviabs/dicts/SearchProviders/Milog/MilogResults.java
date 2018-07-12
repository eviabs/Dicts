
package com.eviabs.dicts.SearchProviders.Milog;

import java.io.Serializable;
import java.util.List;

import com.eviabs.dicts.SearchProviders.Results;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MilogResults extends Results {

    @SerializedName("words")
    @Expose
    private List<Word> words;

    public MilogResults(Integer error, List<Word> words) {
        super(error);
        this.words = words;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    @Override
    public int getCount() {
        return (this.getWords() == null) ? 0 : this.getWords().size();
    }
}
