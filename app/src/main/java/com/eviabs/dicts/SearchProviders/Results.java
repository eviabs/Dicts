package com.eviabs.dicts.SearchProviders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * An abstract class that "wraps" every Results class.
 * The "error" data member is held in here and is hidden from the user.
 */
public abstract class Results {
    @SerializedName("error")
    @Expose
    private int error;

    public Results(int error) {
        this.error = error;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    /**
     * Determines the size of the results object (usually the size of the list that contains the terms/definitions).
     * @return the size of the results.
     */
    public abstract int getCount();

}
