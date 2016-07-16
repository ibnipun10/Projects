package com.UI.mybuddyapp.library;

import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.UI.Common.library.*;
import com.UI.mybuddyapp.library.R;

public class SettingsFragment extends PreferenceFragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        InitializePreferences(v);
        return v;
    }

    private void InitializePreferences(View v) {
        PreferenceCategory preferenceCategory = (PreferenceCategory)findPreference(Constants.SETTINGS_PRIVACYCONTROL_KEY);
        preferenceCategory.setTitle(Constants.SETTINGS_PRIVACYCONTROL_TITLE);

       

        SwitchPreference locationSwitch = (SwitchPreference)findPreference(Constants.SETTINGS_LOCATIONSERVICE_KEY);
        locationSwitch.setTitle(Constants.SETTINGS_LOCATION_SERVICE);
        locationSwitch.setSummary(Constants.SETTINGS_LOCATION_PRIVACY_TEXT);
    }
}