package com.UI.mybuddyapp.library;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.view.Menu;
import com.UI.mybuddyapp.library.R;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
	}



}
