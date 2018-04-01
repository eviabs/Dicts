
package com.eviabs.dicts.Dictionaries.Morfix;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InputLanguageMeaning {

    @SerializedName("DisplayText")
    @Expose
    private String displayText;
    @SerializedName("Translation")
    @Expose
    private String translation;
    @SerializedName("SoundURL")
    @Expose
    private String soundURL;

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getSoundURL() {
        return soundURL;
    }

    public void setSoundURL(String soundURL) {
        this.soundURL = soundURL;
    }

}
