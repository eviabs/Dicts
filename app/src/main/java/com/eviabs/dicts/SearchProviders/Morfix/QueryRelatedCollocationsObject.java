
package com.eviabs.dicts.SearchProviders.Morfix;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QueryRelatedCollocationsObject {

    @SerializedName("phrase")
    @Expose
    private String phrase;
    @SerializedName("trans")
    @Expose
    private String trans;

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

}
