package com.UI.Common.library;


import java.lang.reflect.Field;

import com.UI.mybuddyapp.library.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.opengl.Visibility;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.doubleclick.*;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.drive.internal.e;

public class Utilities {

	public static boolean bKill;

	public static String GetTableName(boolean bHomeTown)
	{
		if(bHomeTown)
			return Constants.HOMETOWN_TABLE;
		else
			return Constants.LOCATION_TABLE;
	}

	public static boolean IsInDebugMode()
	{
		return android.os.Debug.isDebuggerConnected();
	}


	public static void PrintMessageInConsole(String msg)
	{
		if(IsInDebugMode())
		{
			if(msg != null)
				Log.e("Nipun", msg);
			else
				Log.e("Nipun", "msg is null");
		}
	}

	public static String ConvertFriendObjToString(FacebookFriend friend)
	{
		String strFriend = "";

		Field[] fields = friend.getClass().getFields();

		try
		{

			for(int i=0; i<fields.length; i++)
			{
				Field field = fields[i];
				if(field.getType().equals(int.class))
					strFriend += field.getName() + ":" + field.getInt(friend); 
				else if(field.getType().equals(String.class))
				{
					Object value = field.get(friend);
					String strValue = null;
					if(value != null)
						strValue = (String)value;
					strFriend += field.getName() + ":" + strValue;
				}
				else if(field.getType().equals(LocationClass.class))
				{

					Field[] locationFields = field.getType().getFields();

					Object locClassValue = field.get(friend);
					boolean blocClassValueNull = false;
					if(locClassValue == null)
						blocClassValueNull = true;

					for(int j=0; j<locationFields.length; j++)
					{
						Field locfeild = locationFields[j];


						if(locfeild.getType().equals(int.class))
						{
							int value = 0;
							if(!blocClassValueNull)
								value = locfeild.getInt(locClassValue);
							strFriend += locfeild.getName() + ":" + value;
						}
						if(locfeild.getType().equals(double.class))
						{
							double value = 0;
							if(!blocClassValueNull)
							{

								value = locfeild.getDouble(locClassValue);
							}

							strFriend += locfeild.getName() + ":" + value;
						}
						if(locfeild.getType().equals(String.class))
						{
							String value = null;
							if(!blocClassValueNull)
							{
								Object objValue = locfeild.get(locClassValue);
								if(objValue != null)
									value = (String)objValue;

							}
							strFriend += locfeild.getName() + ":" + value;
						}
					}
				}
			}

		}
		catch(Exception ex)
		{
			PrintMessageInConsole(ex.getMessage());
		}

		return strFriend;
	}

	public static String GetKey(double lattitude, double longitude)
	{
		LatLng latlng = new LatLng(lattitude, longitude);
		return latlng.latitude + "," + latlng.longitude;
	}

	public static boolean GetBooleanValueFromPreferences(Context context, String key)
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getBoolean(key, true);
	}

	public static PackageInfo GetPackageInfo(Context context) throws NameNotFoundException {
		PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		return pInfo;
	}

	public static boolean IsPaidApp(Activity activity)
	{
		String strPaid = activity.getString(R.string.Paid);

		if(strPaid.compareTo("true") == 0)
			return true;
		else
			return false;
	}

	public static void LoadAndCreateAd(Activity activity, int id)
	{
		PublisherAdView adView = (PublisherAdView) activity.findViewById(id);

		if(!IsPaidApp(activity))
		{			 
			PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
			adView.loadAd(adRequest);
		}
		else
			adView.setVisibility(View.GONE);
	}	

	public static boolean CheckStringsEquality(String a, String b)
	{
		boolean bMatch = false;
		if(a == null && b == null)
			bMatch = true;
		else if(a == null && b != null)
			bMatch = false;
		else if(a == null && b == null)
			bMatch = false;
		else if(a.equals(b))
			bMatch = true;

		return bMatch;
	}

	public static DBController getDbController(Context context, boolean bHomeTown, String myId) {
		return new DBController(new ContextWrapper(context.getApplicationContext()), Constants.MYBUDDYMAP_DATABASE, Constants.DATABASE_VERSION_ID, bHomeTown, myId);
	}

	public static ProgressDialog ShowProgressDialog(Activity activity) {
		ProgressDialog mDialog = new ProgressDialog(activity);
		mDialog.setMessage("Loading...");
		mDialog.setCancelable(false);
		mDialog.show();
		return mDialog;
	}

	public static void CancelProgressDialog(ProgressDialog mDialog) {
		mDialog.cancel();
	}

	public static void OnBuyButtonSelected (Context context)
	{

		Uri uri = Uri.parse("market://details?id="
				+ Constants.Paid_Package_Name);
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			context.startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			Utilities.PrintMessageInConsole("NipunError" + e.getMessage());


		}

	}
	
	public static void  getOverflowMenu(Context context) {

		try {
			ViewConfiguration config = ViewConfiguration.get(context);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int GetIdentifier(Context context, String name, String type)
	{
		int id = context.getResources().getIdentifier(name, type, context.getPackageName());
		return id;
	}
	
	
}