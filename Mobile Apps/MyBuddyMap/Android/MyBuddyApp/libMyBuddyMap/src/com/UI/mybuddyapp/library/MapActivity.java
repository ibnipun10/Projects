package com.UI.mybuddyapp.library;

import com.UI.Common.library.CommonFacebookClass;
import com.UI.Common.library.Constants;
import com.UI.Common.library.NotificationHelper;
import com.UI.Common.library.Utilities;
import com.UI.mybuddyapp.library.R;
import com.facebook.Session;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MapActivity extends Activity implements OnMarkerClickListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	private NotificationHelper mNotificationHelper;
	private GoogleMap theMap;
	private int mMyIcon, mFriendsIcon;
	private LocationManager mlocMan;
	private final float ZOOM_TO_LEVEL = 4;
	private TextView mtxtProgress;
	private LocationClient mLocationClient;
	private boolean m_bHomeTown;
	private String m_myId;
	private String m_MyName;
	private boolean m_bOnRestart;
	private Session m_session;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Utilities.PrintMessageInConsole("MapActivity onCreate");
		setContentView(R.layout.activity_map);
		
		Bundle values = getIntent().getExtras();
		m_bHomeTown = values.getBoolean(Constants.INTENT_HOMETOWN);
		m_myId = values.getString(Constants.INTENT_MYUSERID);
		m_MyName = values.getString(Constants.INTENT_MYNAME);
		m_bOnRestart = false;
		
		m_session = Session.getActiveSession();
		if(m_session == null)
		{
			m_session = Session.openActiveSession(this, true, null);
		}
		CommonFacebookClass.session = m_session;
		
		Utilities.PrintMessageInConsole("MapActivity onStart");
		InitializeComponents();
		Utilities.LoadAndCreateAd(this, R.id.adView);
		
		
	}

	private void InitializeComponents() {
		// mNotificationHelper = new NotificationHelper(this);
		// mNotificationHelper.createNotification();

		mLocationClient = new LocationClient(this, this, this);

		theMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.the_map)).getMap();
		mMyIcon = R.drawable.red_point;
		mFriendsIcon = R.drawable.green_point;

		mtxtProgress = (TextView) this.findViewById(R.id.idNotification);

		theMap.setOnMarkerClickListener(this);
		SetProgressText(Constants.NOTIFICATION_FB_ANALYSE);	

	}
	
	

	private void CreateThreads() {
		// TODO Auto-generated method stub
		ShowProgressText();
		
		Utilities.bKill = false;
		theMap.clear();
		
		CommonFacebookClass.InitializeCommonFacebookClass(this, m_bHomeTown, m_myId, this);
		
		CommonFacebookClass.m_pfthread = new PopulateFriendsThread(m_bHomeTown);
		CommonFacebookClass.m_pfthread.start();

		CommonFacebookClass.m_plthread = new PopulateLocationThread(
				mNotificationHelper, m_bHomeTown);
		CommonFacebookClass.m_plthread.start();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState)
	{
		Utilities.PrintMessageInConsole("MapActivity onSaveInstanceState");
		super.onSaveInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		Utilities.PrintMessageInConsole("MapActivity onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onRestart()
	{
		Utilities.PrintMessageInConsole("MapActivity onRestart");
		m_bOnRestart = true;
		super.onRestart();
	}
	
	public void ShowLocation(double lattitude, double longitude,
			String placeName) {
		LatLng position = new LatLng(lattitude, longitude);
		MarkerOptions place = new MarkerOptions().position(position)
				.icon(BitmapDescriptorFactory.fromResource(mFriendsIcon))
				.title(placeName)
				.snippet(Utilities.GetKey(lattitude, longitude));

		theMap.addMarker(place);

	}

	public void ShowMyLocation() {

		mlocMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (mlocMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && Utilities.GetBooleanValueFromPreferences(this, Constants.SETTINGS_LOCATIONSERVICE_KEY)) {
			// get last location
			Location lastLoc = mLocationClient.getLastLocation();

			if (lastLoc != null) {
				double lat = lastLoc.getLatitude();
				double lng = lastLoc.getLongitude();
				// create LatLng
				LatLng position = new LatLng(lat, lng);

				MarkerOptions place = new MarkerOptions().position(position)
						.icon(BitmapDescriptorFactory.fromResource(mMyIcon))
						.title("I am Here")
						.snippet(Constants.MY_LOCATIN_SNIPPET);

				theMap.addMarker(place);

				CameraPosition.Builder cameraPositionBuilder = new CameraPosition.Builder();
				cameraPositionBuilder.zoom(ZOOM_TO_LEVEL);
				cameraPositionBuilder.target(position);
				CameraPosition cameraPostion = cameraPositionBuilder.build();

				theMap.animateCamera(
						CameraUpdateFactory.newCameraPosition(cameraPostion),
						3000, null);
			} else {
				// Unable to get the last location
			}
		} else {
			// location service is disabled
		}

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		if (marker.getSnippet().equals(Constants.MY_LOCATIN_SNIPPET))
			return false;

		Intent intent = new Intent(MapActivity.this, BuddylistActivity.class);
		
		intent.putExtra(Constants.DICTIONARY_KEY, marker.getSnippet());
		intent.putExtra(Constants.INTENT_HOMETOWN, m_bHomeTown);
		intent.putExtra(Constants.INTENT_MYUSERID, m_myId);
		intent.putExtra(Constants.INTENT_MYNAME, m_MyName);
		startActivity(intent);

		return false;
	}

	public void SetProgressText(String message) {
		mtxtProgress.setText(message);
	}

	public void HideProgressText() {
		mtxtProgress.setVisibility(View.GONE);
	}
	public void ShowProgressText()
	{
		mtxtProgress.setVisibility(View.VISIBLE);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		try {
			ShowMyLocation();
		} catch (Exception ex) {
			Utilities.PrintMessageInConsole("NipunError" + ex.getMessage());
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStart() {
		
		super.onStart();
		// Connect the client.
		mLocationClient.connect();
		
		if(m_bOnRestart)
		{
			if(!CommonFacebookClass.bDone && Utilities.bKill)
				CreateThreads();
		}
		else
			CreateThreads();
		
	}

	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();		
		Utilities.PrintMessageInConsole("MapActivity onStop");
		super.onStop();
	}
	
	@Override
	protected void onResume()
	{
		Utilities.PrintMessageInConsole("MapActivity onResume");
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{
		Utilities.PrintMessageInConsole("MapActivity onPause");
		super.onPause();
	}
	
	@Override
	protected void onDestroy()
	{
		Utilities.PrintMessageInConsole("MapActivity onDestroy");
		Utilities.bKill = true;
		super.onDestroy();
	}
	

}