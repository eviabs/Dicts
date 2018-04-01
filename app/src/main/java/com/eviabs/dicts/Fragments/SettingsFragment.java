package com.eviabs.dicts.Fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eviabs.dicts.Activities.MainActivity;
import com.eviabs.dicts.R;
import com.eviabs.dicts.Utils.LocalPreferences;
import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompatDividers;

public class SettingsFragment extends PreferenceFragmentCompatDividers {

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        // additional setup
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            return super.onCreateView(inflater, container, savedInstanceState);
        } finally {

            Preference version = (Preference)  getPreferenceManager().findPreference("version");
            EditTextPreference customServer = (EditTextPreference)  getPreferenceManager().findPreference("custom_server_url");

            // Try to update the version
            try {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                version.setSummary(String.format(getActivity().getResources().getString(R.string.settings_version_string),  pInfo.versionName, pInfo.versionCode));

            } catch (PackageManager.NameNotFoundException e) {
                version.setSummary("");
                e.printStackTrace();
            }

            // Make sure that ONLY legal urls are committed.
            // Bad URLs will raise an exception in Retrofit.
            customServer.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                                           @Override
                                                           public boolean onPreferenceChange(Preference preference, Object newValue) {
                                                               if (!LocalPreferences.isValidURL((String)newValue)) {
                                                                   ((MainActivity)getActivity()).showToast(getString(R.string.settings_bad_url));
                                                                   return false;
                                                               }

                                                               return true;
                                                           }
                                                       });

            setDividerPreferences(DIVIDER_PADDING_CHILD | DIVIDER_CATEGORY_AFTER_LAST | DIVIDER_CATEGORY_BETWEEN);
        }
    }
}
