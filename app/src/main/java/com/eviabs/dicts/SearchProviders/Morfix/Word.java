
package com.eviabs.dicts.SearchProviders.Morfix;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Word {

    @SerializedName("InputLanguageMeanings")
    @Expose
    private List<List<InputLanguageMeaning>> inputLanguageMeanings = null;
    @SerializedName("OutputLanguageMeanings")
    @Expose
    private List<List<OutputLanguageMeaning>> outputLanguageMeanings = null;
    @SerializedName("PartOfSpeech")
    @Expose
    private String partOfSpeech;
    @SerializedName("OutputLanguageMeaningsString")
    @Expose
    private String outputLanguageMeaningsString;
    @SerializedName("Inflections")
    @Expose
    private List<Inflection> inflections = null;
    @SerializedName("SampleSentences")
    @Expose
    private List<String> sampleSentences = null;
    @SerializedName("SynonymsList")
    @Expose
    private List<String> synonymsList = null;

    public List<List<InputLanguageMeaning>> getInputLanguageMeanings() {
        return inputLanguageMeanings;
    }

    public void setInputLanguageMeanings(List<List<InputLanguageMeaning>> inputLanguageMeanings) {
        this.inputLanguageMeanings = inputLanguageMeanings;
    }

    public List<List<OutputLanguageMeaning>> getOutputLanguageMeanings() {
        return outputLanguageMeanings;
    }

    public void setOutputLanguageMeanings(List<List<OutputLanguageMeaning>> outputLanguageMeanings) {
        this.outputLanguageMeanings = outputLanguageMeanings;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getOutputLanguageMeaningsString() {
        return outputLanguageMeaningsString;
    }

    public void setOutputLanguageMeaningsString(String outputLanguageMeaningsString) {
        this.outputLanguageMeaningsString = outputLanguageMeaningsString;
    }

    public List<Inflection> getInflections() {
        return inflections;
    }

    public void setInflections(List<Inflection> inflections) {
        this.inflections = inflections;
    }

    public List<String> getSampleSentences() {
        return sampleSentences;
    }

    public void setSampleSentences(List<String> sampleSentences) {
        this.sampleSentences = sampleSentences;
    }

    public List<String> getSynonymsList() {
        return synonymsList;
    }

    public void setSynonymsList(List<String> synonymsList) {
        this.synonymsList = synonymsList;
    }

}
