package com.product.physioit;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.speech.tts.UtteranceProgressListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Common.Constants;
import Common.Utilities;

/**
 * Created by nipuna on 9/7/13.
 */
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

        SwitchPreference azureswitch = (SwitchPreference)findPreference(Constants.SETTINGS_AZURESERVICE_KEY);
        azureswitch.setTitle(Constants.SETTINGS_AZURE_UPDATE);
        azureswitch.setSummary(Constants.SETTINGS_DATA_PRIVACY_TEXT);


        SwitchPreference locationSwitch = (SwitchPreference)findPreference(Constants.SETTINGS_LOCATIONSERVICE_KEY);
        locationSwitch.setTitle(Constants.SETTINGS_LOCATION_SERVICE);
        locationSwitch.setSummary(Constants.SETTINGS_LOCATION_PRIVACY_TEXT);
    }
}