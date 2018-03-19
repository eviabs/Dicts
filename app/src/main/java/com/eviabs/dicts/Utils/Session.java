package com.eviabs.dicts.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private static final String USER_NAME = "username";
    private static final String USER_ID = "userid";
    private static final String SETTINGS_NAVIGATION = "navigation";
    private static final String SETTINGS_LOCAL_SERVER = "server";
    private static final String SETTINGS_SHOW_ALL_RECORDINGS = "show_all";

    public static final String NO_USER_NAME = "";
    public static final int NO_USER_ID = -1;
    public static final boolean NO_SETTINGS_NAVIGATION = false;
    public static final boolean NO_SETTINGS_LOCAL_SERVER = false;
    public static final boolean NO_SETTINGS_SHOW_ALL_RECORDINGS = false;

    private SharedPreferences prefs;

    public Session(Context context) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getUserName() {
        return prefs.getString(USER_NAME, NO_USER_NAME);
    }
    public void setUserName(String userName) {
        prefs.edit().putString(USER_NAME, userName).apply();
    }


    public int getUserID() {
        return prefs.getInt(USER_ID, NO_USER_ID);
    }
    public void setUserID(int userID) {
        prefs.edit().putInt(USER_ID, userID).apply();
    }


    public void logIn(String username, int userID) {
        setUserName(username);
        setUserID(userID);
    }
    public void logOut() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
    public boolean isLoggedIn() {
        return (prefs.getInt(USER_ID, NO_USER_ID) != NO_USER_ID);
    }

    public boolean getSettingsNavigation() {
        return prefs.getBoolean(SETTINGS_NAVIGATION, NO_SETTINGS_NAVIGATION);
    }
    public void setSettingsNavigation(boolean navigation) {
        prefs.edit().putBoolean(SETTINGS_NAVIGATION, navigation).apply();
    }

    public boolean getSettingsServer() {
        return prefs.getBoolean(SETTINGS_LOCAL_SERVER, NO_SETTINGS_LOCAL_SERVER);
    }
    public void setSettingsServer(boolean server) {
        prefs.edit().putBoolean(SETTINGS_LOCAL_SERVER, server).apply();
    }

    public boolean getSettingsShowAllRecordings() {
        return prefs.getBoolean(SETTINGS_SHOW_ALL_RECORDINGS, NO_SETTINGS_SHOW_ALL_RECORDINGS);
    }
    public void setSettingsShowAllRecordings(boolean showAllRecordings) {
        prefs.edit().putBoolean(SETTINGS_SHOW_ALL_RECORDINGS, showAllRecordings).apply();
    }
    //TODO: fix this
//    public void apllySettings() {
//        StoryServerURLs.serverURL = (getSettingsServer() ?  StoryServerURLs.serverURLLocal : StoryServerURLs.serverURLServer);
//    }
}