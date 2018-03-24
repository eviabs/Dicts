package com.eviabs.dicts.Dictionaries.UrbanDictionary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UrbanDictionaryResults {
    @SerializedName("error")
    @Expose
    private int error;

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
        this.error = error;
        this.result_type = result_type;
        this.list = list;
        this.sounds = sounds;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
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
}
