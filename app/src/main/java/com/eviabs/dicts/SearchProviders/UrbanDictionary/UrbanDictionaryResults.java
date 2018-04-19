package com.eviabs.dicts.SearchProviders.UrbanDictionary;

import com.eviabs.dicts.SearchProviders.Results;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UrbanDictionaryResults extends Results {
    @SerializedName("result_type")
    @Expose
    private String result_type;

    @SerializedName("list")
    @Expose
    private List<UrbanDictionaryTerm> list;

    @SerializedName("sounds")
    @Expose
    private List<String> sounds;

    public UrbanDictionaryResults(int error, String result_type, List<UrbanDictionaryTerm> list, List<String> sounds) {
        super(error);
        this.result_type = result_type;
        this.list = list;
        this.sounds = sounds;
    }

    public String getResult_type() {
        return result_type;
    }

    public void setResult_type(String result_type) {
        this.result_type = result_type;
    }

    public List<UrbanDictionaryTerm> getList() {
        return list;
    }

    public void setList(List<UrbanDictionaryTerm> list) {
        this.list = list;
    }

    public List<String> getSounds() {
        return sounds;
    }

    public void setSounds(List<String> sounds) {
        this.sounds = sounds;
    }

    @Override
    public int getCount() {
        return this.getList().size();
    }
}
