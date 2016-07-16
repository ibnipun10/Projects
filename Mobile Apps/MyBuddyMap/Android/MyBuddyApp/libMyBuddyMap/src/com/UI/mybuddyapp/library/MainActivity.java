package com.UI.mybuddyapp.library;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.UI.Common.library.CommonFacebookClass;
import com.UI.Common.library.Utilities;
import com.UI.mybuddyapp.library.R;
import com.facebook.UiLifecycleHelper;

import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.FragmentActivity;

import com.facebook.model.GraphUser;
import com.facebook.widget.*;



public class MainActivity extends FragmentActivity {

	private UiLifecycleHelper uiHelper;
	private LoginButton loginButton;
	private GraphUser user;
	private MainFragment mainFragment;
	private Menu mMenu;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		Utilities.getOverflowMenu(this);
		
		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			mainFragment = new MainFragment();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, mainFragment).commit();
		} else {
			// Or set the fragment from restored state info
			mainFragment = (MainFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
		
		
	}
	

	
	private void GetKeyHashForApp() {
		// TODO Auto-generated method stub
		
		// Add code to print out the key hash
	    try {
	        PackageInfo info =  getPackageManager().getPackageInfo(
	        		this.getPackageName(), 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            String key = Base64.encodeToString(md.digest(), Base64.DEFAULT);
	            Log.d("Key", key);
	            Log.e("KeyValue", key);
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
		
	}

	protected void onResume() {
		super.onResume();
		Utilities.bKill = true;
		try {

			if (CommonFacebookClass.m_pfthread != null) {
				if (CommonFacebookClass.m_pfthread.isAlive())
					CommonFacebookClass.m_pfthread.interrupt();
				CommonFacebookClass.m_pfthread = null;
				
			}
			if (CommonFacebookClass.m_plthread != null) {
				if (CommonFacebookClass.m_plthread.isAlive())
					CommonFacebookClass.m_plthread.interrupt();
				
				CommonFacebookClass.m_plthread = null;
			}
			
			
		} catch (Exception ex) {
			Utilities.PrintMessageInConsole("NipunError" + ex.getMessage());
			Utilities.PrintMessageInConsole("On REsume : " + Log.getStackTraceString(ex));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		mMenu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent;
		int itemId = item.getItemId();
		if (itemId == Utilities.GetIdentifier(this, "menu_about", "id") ){
			intent = new Intent(MainActivity.this, AboutActivity.class);
			startActivity(intent);
		} else if (itemId == Utilities.GetIdentifier(this, "menu_settings", "id")) {
			intent = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intent);
		} else if (itemId == Utilities.GetIdentifier(this, "menu_buy", "id")) {
			Utilities.OnBuyButtonSelected(this);
		}
		return true;
	}
}
