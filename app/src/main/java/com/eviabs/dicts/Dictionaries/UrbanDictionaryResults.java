package com.eviabs.dicts.Dictionaries;

import java.util.ArrayList;

public class UrbanDictionaryResults {
    int error;
    String result_type;
    ArrayList<UrbanDictionaryTerm> list;
    ArrayList<String> sounds;

    public UrbanDictionaryResults(int error, String result_type, ArrayList<UrbanDictionaryTerm> list, ArrayList<String> sounds) {
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

    public ArrayList<UrbanDictionaryTerm> getList() {
        return list;
    }

    public void setList(ArrayList<UrbanDictionaryTerm> list) {
        this.list = list;
    }

    public ArrayList<String> getSounds() {
        return sounds;
    }

    public void setSounds(ArrayList<String> sounds) {
        this.sounds = sounds;
    }
}
