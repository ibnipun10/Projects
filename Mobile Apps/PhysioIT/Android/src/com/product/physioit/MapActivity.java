package com.product.physioit;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.location.LocationListener;
import android.util.Log;

import Common.Constants;

/**
 * Created by nipuna on 9/4/13.
 */


public class MapActivity extends Activity implements LocationListener, 		GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private int userIcon, clinicIcon;
    //instance variables for Marker icon drawable resources
    //the map
    private GoogleMap theMap;

    //location manager
    private LocationManager locMan;

    //user marker
    private Marker userMarker;

    //places of interest
    private Marker[] placeMarkers;
    private LocationClient mLocationClient;
    //max
    private final int MAX_PLACES = 20;//most returned from google
    //marker options
    private MarkerOptions[] places;
    private final float ZOOM_TO_LEVEL = 10;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        mLocationClient = new LocationClient(this, this, this);
        locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        userIcon = R.drawable.green_point;
        clinicIcon = R.drawable.red_point;

        try {
            //find out if we already have it
            if (theMap == null) {
                //get the map
                theMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.the_map)).getMap();
                //check in case map/ Google Play services not available
                if (theMap != null) {
                    //ok - proceed
                    theMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    //create marker array
                    placeMarkers = new Marker[MAX_PLACES];
                    //update location

                }

            }
        } catch (Exception ex) {
            Log.i("MapActivity", ex.getMessage());
        }
    }

    //location listener functions

    @Override
    public void onLocationChanged(Location location) {
        Log.v("MapActivity", "location changed");
        // updatePlaces();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.v("MapActivity", "provider disabled");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.v("MapActivity", "provider enabled");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.v("MapActivity", "status changed");
    }

    /*
     * update the place markers
     */
    private void updatePlaces() throws Exception {
        //get location manager

        if (locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //get last location
            Location lastLoc = mLocationClient.getLastLocation();
            double lat = lastLoc.getLatitude();
            double lng = lastLoc.getLongitude();
            //create LatLng
            LatLng lastLatLng = new LatLng(lat, lng);

            //remove any existing marker
            if (userMarker != null) userMarker.remove();
            //create and set marker properties
            userMarker = theMap.addMarker(new MarkerOptions()
                    .position(lastLatLng)
                    .title("You are here")
                    .icon(BitmapDescriptorFactory.fromResource(userIcon))
                    .snippet("Your last recorded location"));
            //move to location

            CameraPosition.Builder cameraPositionBuilder = new CameraPosition.Builder();
            cameraPositionBuilder.zoom(ZOOM_TO_LEVEL);
            cameraPositionBuilder.target(lastLatLng);
            CameraPosition cameraPostion = cameraPositionBuilder.build();

            theMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPostion), 3000, null);


            //build places query string
            String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                    "json?location=" + lat + "," + lng +
                    "&radius=50000&sensor=true" +
                    "&types=" + Constants.MAP_PHYSIOTHERAPY_CLINIC +
                    "&key=" + Constants.MAP_PLACES_ACCESS_KEY;//ADD KEY

            //execute query
            new GetPlaces().execute(placesSearchStr);

            locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
        } else
            throw new Exception("Location service is disabled");
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            updatePlaces();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class GetPlaces extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... placesURL) {
            //fetch places

            //build result as string
            StringBuilder placesBuilder = new StringBuilder();
            //process search parameter string(s)
            for (String placeSearchURL : placesURL) {
                HttpClient placesClient = new DefaultHttpClient();
                try {
                    //try to fetch the data

                    //HTTP Get receives URL string
                    HttpGet placesGet = new HttpGet(placeSearchURL);
                    //execute GET with Client - return response
                    HttpResponse placesResponse = placesClient.execute(placesGet);
                    //check response status
                    StatusLine placeSearchStatus = placesResponse.getStatusLine();
                    //only carry on if response is OK
                    if (placeSearchStatus.getStatusCode() == 200) {
                        //get response entity
                        HttpEntity placesEntity = placesResponse.getEntity();
                        //get input stream setup
                        InputStream placesContent = placesEntity.getContent();
                        //create reader
                        InputStreamReader placesInput = new InputStreamReader(placesContent);
                        //use buffered reader to process
                        BufferedReader placesReader = new BufferedReader(placesInput);
                        //read a line at a time, append to string builder
                        String lineIn;
                        while ((lineIn = placesReader.readLine()) != null) {
                            placesBuilder.append(lineIn);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return placesBuilder.toString();
        }

        //process data retrieved from doInBackground
        protected void onPostExecute(String result) {
            //parse place data returned from Google Places
            //remove existing markers
            if (placeMarkers != null) {
                for (int pm = 0; pm < placeMarkers.length; pm++) {
                    if (placeMarkers[pm] != null)
                        placeMarkers[pm].remove();
                }
            }
            try {
                //parse JSON

                //create JSONObject, pass stinrg returned from doInBackground
                JSONObject resultObject = new JSONObject(result);
                //get "results" array
                JSONArray placesArray = resultObject.getJSONArray("results");
                //marker options for each place returned
                places = new MarkerOptions[placesArray.length()];
                //loop through places
                for (int p = 0; p < placesArray.length(); p++) {
                    //parse each place
                    //if any values are missing we won't show the marker
                    boolean missingValue = false;
                    LatLng placeLL = null;
                    String placeName = "";
                    String vicinity = "";
                    int currIcon = clinicIcon;
                    try {
                        //attempt to retrieve place data values
                        missingValue = false;
                        //get place at this index
                        JSONObject placeObject = placesArray.getJSONObject(p);
                        //get location section
                        JSONObject loc = placeObject.getJSONObject("geometry")
                                .getJSONObject("location");
                        //read lat lng
                        placeLL = new LatLng(Double.valueOf(loc.getString("lat")),
                                Double.valueOf(loc.getString("lng")));

                        //vicinity
                        vicinity = placeObject.getString("vicinity");
                        //name
                        placeName = placeObject.getString("name");
                    } catch (JSONException jse) {
                        Log.v("PLACES", "missing value");
                        missingValue = true;
                        jse.printStackTrace();
                    }
                    //if values missing we don't display
                    if (missingValue) places[p] = null;
                    else
                        places[p] = new MarkerOptions()
                                .position(placeLL)
                                .title(placeName)
                                .icon(BitmapDescriptorFactory.fromResource(currIcon))
                                .snippet(vicinity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (places != null && placeMarkers != null) {
                for (int p = 0; p < places.length && p < placeMarkers.length; p++) {
                    //will be null if a value was missing
                    if (places[p] != null)
                        placeMarkers[p] = theMap.addMarker(places[p]);
                }
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (theMap != null) {
            locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (theMap != null) {
            locMan.removeUpdates(this);
        }
    }
}