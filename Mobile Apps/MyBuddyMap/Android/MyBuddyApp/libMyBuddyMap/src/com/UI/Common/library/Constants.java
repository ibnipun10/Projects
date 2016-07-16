package com.UI.Common.library;

import com.facebook.model.GraphUser;

public class Constants {

	public static final int SECONDS_TO_WAIT = 20;

	//Notification Message
	public static final String NOTIFICATION_FB_ANALYSE = "Retrieving facebook friends";
	public static final String NOTIFICATION_FB_ERROR = "Unable to show your location";	
	public static final String PROGRESS_TEXT = "%s out of %s friends mapped, Updating %s-%s"; 
	public static final String FINAL_MAPPED_MESSAGE = "Total friends mapped %s";

	public static final String DICTIONARY_KEY = "Key";
	public static final String MY_LOCATIN_SNIPPET = "My Location";

	public static final String BUDDYLIST_TITLE = "%s friend(s)";

	//Settings Application Data
	public static final String SETTINGS_TEXT = "Settings";
	public static final String SETTINGS_LOCATION_SERVICE = "LocationService";
	public static final String SETTINGS_YES = "1";
	public static final String SETTINGS_NO = "0";
	public static final String SETTINGS_PRIVACYCONTROL_TITLE = "Privacy Control";
	public static final String SETTINGS_LOCATION_PRIVACY_TEXT = "This application uses your current location for mapping services. You may disable this service any time";
	public static final String SETTINGS_PRIVACYCONTROL_KEY = "prefCategory";
	public static final String SETTINGS_LOCATIONSERVICE_KEY = "prefSwitchLocationService";

	public static final String MAINPAGE_MESSAGE = "This app will mark your facebook friends on the world map as per their current location or hometown. Tapping on the mark will show you the list of friends in that location";

	public static final String ABOUT_TEXT = "This is a utility app which marks your facebook friends on the world map. Based on the current location or hometown as updated by your friends on facebook, it will mark them on a map. You can also tap on the mark to see the list of friends in that location. To see your location on map, please enable location service in your phone settings";
	public static final  String APP_VERSION = "Version";
	public static final  String APP_AUTHOR = "Author";
	public static final String APP_AUTHOR_NAME = "Nipun Agarwal";
	public static final  String ABOUT_RATE_BUTTON = "Rate it!";

	public static final String MY_EMAIL = "ibnipun10@gmail.com";
	public static final String EMAIL_SUBJECT = "MyBuddyMap Feedback";
	public static final String EMAIL_ATTACHMENT_SUBJECT = "MyBuddyMap Buddy List";
	
	//Databse Name and version
	public static final  String MYBUDDYMAP_DATABASE = "myBuddyMapdb";
    public static final  int DATABASE_VERSION_ID = 1;
    public static final String HOMETOWN_TABLE = "HOMETOWN_TBL";
    public static final String LOCATION_TABLE = "LOCATION_TBL";
    
    public static final String INTENT_HOMETOWN = "Intent_hometown";
    public static final String INTENT_MYUSERID = "MyUserId";
    public static final String INTENT_MYNAME = "MyName";
    
    //Messages from database
    public static final String DB_READING_DB = "Getting Stored list of friends";
    public static final String DB_FRIENDS_RECEIVED = "%s friends retrieved from storage";
    public static final String FB_FRIEDNS_RECEIVED = "%s friends retrieved from facebook";
    public static final String FB_READING_FB = "Getting list from facebook";
   
    public static final String Paid_Package_Name = "com.UI.mybuddyappPaid";
    public static final String UNMAPEED_FRIEND = "UnMapped Friends";
    public static final String PIE_CHART_TITLE = "Friends Density";
    
    //TOast
    public static final String TOAST_NO_CHART_SELECTED = "No chart element was clicked";
    
    //Graph
    public static final String GRAPH_ACTIVTTY_TITLE = "%s Locations";
}
