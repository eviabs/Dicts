package com.eviabs.dicts.Dictionaries.Morfix;

import java.util.List;

import com.eviabs.dicts.Dictionaries.Results;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// created using http://www.jsonschema2pojo.org/

public class MorfixResults extends Results {

    @SerializedName("ResultType")
    @Expose
    private String resultType;
    @SerializedName("Query")
    @Expose
    private String query;
    @SerializedName("TranslationType")
    @Expose
    private String translationType;
    @SerializedName("CorrectionList")
    @Expose
    private List<Object> correctionList = null;
    @SerializedName("Words")
    @Expose
    private List<Word> words = null;
    @SerializedName("QueryRelatedCollocations")
    @Expose
    private List<String> queryRelatedCollocations = null;
    @SerializedName("QueryRelatedPhrasalVerbs")
    @Expose
    private List<QueryRelatedPhrasalVerb> queryRelatedPhrasalVerbs = null;
    @SerializedName("QueryRelatedCollocationsObject")
    @Expose
    private List<QueryRelatedCollocationsObject> queryRelatedCollocationsObject = null;

    public MorfixResults(int error) {
        super(error);
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTranslationType() {
        return translationType;
    }

    public void setTranslationType(String translationType) {
        this.translationType = translationType;
    }

    public List<Object> getCorrectionList() {
        return correctionList;
    }

    public void setCorrectionList(List<Object> correctionList) {
        this.correctionList = correctionList;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public List<String> getQueryRelatedCollocations() {
        return queryRelatedCollocations;
    }

    public void setQueryRelatedCollocations(List<String> queryRelatedCollocations) {
        this.queryRelatedCollocations = queryRelatedCollocations;
    }

    public List<QueryRelatedPhrasalVerb> getQueryRelatedPhrasalVerbs() {
        return queryRelatedPhrasalVerbs;
    }

    public void setQueryRelatedPhrasalVerbs(List<QueryRelatedPhrasalVerb> queryRelatedPhrasalVerbs) {
        this.queryRelatedPhrasalVerbs = queryRelatedPhrasalVerbs;
    }

    public List<QueryRelatedCollocationsObject> getQueryRelatedCollocationsObject() {
        return queryRelatedCollocationsObject;
    }

    public void setQueryRelatedCollocationsObject(List<QueryRelatedCollocationsObject> queryRelatedCollocationsObject) {
        this.queryRelatedCollocationsObject = queryRelatedCollocationsObject;
    }

    @Override
    public int getCount() {
        return this.getWords().size();
    }
}
