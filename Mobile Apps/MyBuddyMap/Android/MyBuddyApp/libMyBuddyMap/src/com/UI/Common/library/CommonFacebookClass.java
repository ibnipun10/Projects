package com.UI.Common.library;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.protocol.RequestAddCookies;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.hardware.Camera.Face;
import android.util.Log;

import com.UI.mybuddyapp.library.BuddylistActivity;
import com.UI.mybuddyapp.library.MapActivity;
import com.UI.mybuddyapp.library.PopulateFriendsThread;
import com.UI.mybuddyapp.library.PopulateLocationThread;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.Facebook;
import com.facebook.model.GraphObject;
import com.facebook.widget.FriendPickerFragment;
import com.google.android.gms.internal.bk;

public class CommonFacebookClass {

	public static Session session;
	public static List<String> lfriends;
	public static LocationClass sLocation;
	public static FacebookFriend fbFriend;
	public static Queue<FacebookFriend> queueFriends;
	public static boolean bDone;
	public static PopulateFriendsThread m_pfthread;
	public static PopulateLocationThread m_plthread;
	private static Dictionary<String, FacebookFriend> m_dictDBFriends;
	private static List<String> m_lisoffFriendsRemoveFromDB;
	public static Integer m_iTotalFriendsCount;
	public static Integer m_iTotalFriendsMapped;
	public static DBController m_dbController;
	private static boolean m_bHomeTown;
	private static String m_myId;
	private static MapActivity m_MapActivity;
	public static int m_iCountFBFriendsUpdation;
	public static int m_iCountFBFriends;
	public static BuddylistActivity m_BuddyListActivity;
	
	/* This is used for telling status to buddylistActivity 
	 *  Check for the status to be 2 for complete 
	 *  0 : Uninitialized state
	 *  1 : Started
	 *  2 : Finished */
	
	public static int m_iStateofthreads;

	public static void InitializeCommonFacebookClass(Context context, boolean bHomeTown, String myId, MapActivity mapActivity)
	{
		m_iTotalFriendsCount = new Integer(0);
		m_iTotalFriendsMapped = new Integer(0);

		queueFriends = new LinkedList<FacebookFriend>();
		sLocation = null;
		bDone = false;
		lfriends = null;
		fbFriend = null;
		m_lisoffFriendsRemoveFromDB = null;
		m_bHomeTown = bHomeTown;
		m_myId = myId;
		m_MapActivity = mapActivity;
		m_iCountFBFriendsUpdation = 0;
		m_BuddyListActivity = null;
		m_iStateofthreads = 1;


		m_dbController = Utilities.getDbController(context, m_bHomeTown, myId);

	}

	public static void IncrementTotalFriendsCount(boolean btrue)
	{
		synchronized (m_iTotalFriendsCount) {
			if(btrue)
				m_iTotalFriendsCount++;
			else
				m_iTotalFriendsCount--;
		}
	}

	public static void IncrementFriendsMapped(boolean btrue)
	{
		synchronized (m_iTotalFriendsMapped) {
			if(btrue)
				m_iTotalFriendsMapped++;
			else
				m_iTotalFriendsMapped--;
		}
	}

	public static void GetAllFriendsFromDB() throws Exception
	{

		m_dictDBFriends = new Hashtable<String, FacebookFriend>();
		m_lisoffFriendsRemoveFromDB = new ArrayList<String>();

		List<FacebookFriend> lfbFriend = m_dbController.ReadRowsInTable(null);
		Utilities.PrintMessageInConsole("Count of friends from DB : " + lfbFriend.size());
		m_iTotalFriendsCount = lfbFriend.size();
		SetProgressBarTextByThread(String.format(Constants.DB_FRIENDS_RECEIVED, m_iTotalFriendsCount));

		//copy the list to here
		for(int i=0; i< lfbFriend.size(); i++)
		{
			InsertIntoDBDictionary(lfbFriend.get(i), true);
		}
	}

	public static void UpdateListofFriendsTobeDeletedFromDB(String id)
	{
		m_lisoffFriendsRemoveFromDB.remove(id);
	}

	public static void InsertIntoDBDictionary(FacebookFriend fbfriend, boolean bFromDB) throws Exception
	{
		FacebookFriend item = m_dictDBFriends.get(fbfriend.id);
		fbfriend.myId = m_myId;

		if(item == null)
		{
			m_dictDBFriends.put(fbfriend.id, fbfriend);


			if(!bFromDB)
			{
				//Insert into db as this is a new friend found.
				m_dbController.InsertRowInTable(fbfriend);
				IncrementTotalFriendsCount(true);
			}
			else
			{

				m_lisoffFriendsRemoveFromDB.add(fbfriend.id);
			}

			AddToQueue(fbfriend);
		}
		else
		{
			//Already present, check for whether its updated.
			if(item.location.id == null && fbfriend.location != null)
			{
				UpdateAllLists(fbfriend);
			}
			else if(item.location.id != null && fbfriend.location != null)
			{
				if(!MatchLists(item, fbfriend))
				{
					UpdateAllLists(fbfriend);
				}
			}
			else
			{				
				UpdateProgressTextByThread();
			}

		}
	}

	private static void UpdateAllLists(FacebookFriend fbfriend) throws Exception
	{

		//remove the friend from map
		m_dictDBFriends.put(fbfriend.id, null);

		//update into db		
		m_dbController.UpdateRowInTable(fbfriend);

		//Insert into db dictionary
		InsertIntoDBDictionary(fbfriend, false);
	}

	public static  boolean MatchLists(FacebookFriend a, FacebookFriend b) throws IllegalAccessException, IllegalArgumentException
	{
		boolean bmatch = true;
		Field[] fields = a.getClass().getFields();

		for(int i =0; i<fields.length; i++)
		{
			Field field = fields[i];

			if(field.getType().equals(int.class))
			{
				if(field.getInt(a) != field.getInt(b))
					return false;
			}
			if(field.getType().equals(String.class))
			{
				String stra = (String)field.get(a);
				String strb = (String)field.get(b);
				if(!Utilities.CheckStringsEquality(stra, strb))
					return false;
			}
			if(field.getType().equals(LocationClass.class))
			{
				Field[] locfields = field.getClass().getFields();
				Object obja = field.get(a);
				Object objb = field.get(b);

				if(obja == null && objb != null)
				{
					LocationClass lc = (LocationClass)objb;
					if(lc.id == null)
						return true;
					else
						return false;
				}
				if(objb == null && obja != null)
				{
					LocationClass lc = (LocationClass)obja;
					if(lc.id == null)
						return true;
					else
						return false;
				}
				if(obja == null && objb == null)
					return true;

				for(int j=0; j< locfields.length; j++)
				{
					Field locfield = locfields[j];
					if(locfield.getType().equals(String.class))
					{
						String stra = (String)field.get(a);
						String strb = (String)field.get(b);
						if(!Utilities.CheckStringsEquality(stra,  strb))
							return false;
					}
					if(locfield.getType().equals(Double.class))
					{
						Double dba = field.getDouble(a);
						Double dbb = field.getDouble(b);
						if(!dba.equals(dbb))
							return false;
					}
				}
			}


		}

		return bmatch;		

	}

	public static void GetAllFriends() throws InterruptedException {

		lfriends = new ArrayList<String>();
		String strRequest = "me/friends";
		if(session == null || !session.isOpened())
		{
			Utilities.PrintMessageInConsole("Unable to open facebook session");
			SetProgressBarTextByThread("Unable to connect to facebook");
			Utilities.bKill = true;
		}

		Request request = new Request(session, strRequest, null, null,
				new Request.Callback() {
			public void onCompleted(Response response) {
				GraphObject graphObject = response.getGraphObject();
				FacebookRequestError error = response.getError();

				if (graphObject != null) {
					JSONObject jsonObject = graphObject
							.getInnerJSONObject();
					try {
						JSONArray array = jsonObject
								.getJSONArray("data");
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = (JSONObject) array
									.get(i);
							lfriends.add(object.get("id").toString());
						}
					} catch (JSONException e) {

						e.printStackTrace();
						Utilities.PrintMessageInConsole(e.getMessage());
					}
				}

			}
		});

		// RequestAsyncTask task = new RequestAsyncTask(request);
		// task.execute();
		// request.executeAsync();
		request.executeAndWait();

	}

	public static FacebookFriend GetFriendsDetails(String strId)
			throws InterruptedException {

		String strRequest = strId;
		fbFriend = null;

		Request request = new Request(session, strRequest, null, null,
				new Request.Callback() {
			public void onCompleted(Response response) {
				GraphObject graphObject = response.getGraphObject();
				FacebookRequestError error = response.getError();

				if (graphObject != null) {
					JSONObject jsonObject = graphObject
							.getInnerJSONObject();
					try {

						fbFriend = PopulateFaceBookObject(jsonObject,
								new FacebookFriend());

					} catch (Exception e) {

						e.printStackTrace();
						Utilities.PrintMessageInConsole("NipunError" + e.getMessage());
					}
				}

			}
		});

		request.executeAndWait();

		return fbFriend;
	}

	public static LocationClass GetLocationDetails(LocationClass location)
			throws InterruptedException {

		if (location == null)
			return null;

		String strRequest = location.id;
		sLocation = location;

		Request request = new Request(session, strRequest, null, null,
				new Request.Callback() {
			public void onCompleted(Response response) {
				GraphObject graphObject = response.getGraphObject();
				FacebookRequestError error = response.getError();

				if (graphObject != null) {
					JSONObject jsonObject = graphObject
							.getInnerJSONObject();
					try {

						JSONObject from = (JSONObject) jsonObject
								.get("location");
						if (from != null) {
							sLocation.lattitude =  from
									.getDouble("latitude");
							sLocation.longitude =  from
									.getDouble("longitude");
						}

					} catch (Exception e) {

						e.printStackTrace();
						Utilities.PrintMessageInConsole(e.getMessage());
					}
				}

			}
		});

		request.executeAndWait();

		return sLocation;
	}

	private static void CountDownAwait(CountDownLatch countDownLatch)
			throws InterruptedException {
		countDownLatch.await(Constants.SECONDS_TO_WAIT, TimeUnit.SECONDS);
	}

	private static <T> T PopulateFaceBookObject(JSONObject object, T className)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, JSONException {
		T obj = (T) className.getClass().newInstance();

		Field[] fields = obj.getClass().getFields();

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (field.getType().equals(LocationClass.class)) {
				if (m_bHomeTown) {
					if (object.has("hometown")) {
						JSONObject from = (JSONObject) object.get("hometown");
						field.set(
								obj,
								PopulateFaceBookObject(from,
										new LocationClass()));
					}
				} else {
					if (object.has("location")) {
						JSONObject from = (JSONObject) object.get("location");
						field.set(
								obj,
								PopulateFaceBookObject(from,
										new LocationClass()));
					}
				}
			}

			else if (object.has(field.getName())) {
				field.set(obj, object.getString(field.getName()));
			}

		}

		return obj;
	}

	public static URL GetFacebookPicURL(String id) throws MalformedURLException {
		return new URL("https://graph.facebook.com/" + id + "/picture");
	}

	public static void AddToQueue(FacebookFriend item) throws Exception {

		if(queueFriends != null)
		{
			synchronized (queueFriends) {
				queueFriends.add(item);
			}
		}
		else
			throw new Exception("Queue is null");
	}

	public static FacebookFriend RemoveItemInQueue() {
		FacebookFriend item = null;

		try
		{
			synchronized (queueFriends) {
				item = queueFriends.remove();
			}
		}
		catch (Exception ex)
		{
			Utilities.PrintMessageInConsole(Log.getStackTraceString(ex));
		}

		return item;
	}

	public static void ShowLocationOnMapByThread(final FacebookFriend friend)
	{
		m_MapActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try
				{
					if(m_MapActivity != null)
						m_MapActivity.ShowLocation(friend.location.lattitude, friend.location.longitude, friend.location.name);
					else
					{
						Utilities.PrintMessageInConsole("Map activity is null");
						Utilities.bKill = true;
					}
				}
				catch(Exception ex)
				{
					Utilities.PrintMessageInConsole(Log.getStackTraceString(ex));
				}

			}

		});

	}

	public static void SetProgressBarTextByThread(final String message)
	{
		m_MapActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try
				{
					m_MapActivity.SetProgressText(message);
				}
				catch(Exception ex)
				{
					Utilities.PrintMessageInConsole(Log.getStackTraceString(ex));
				}

			}

		});

	}

	public static void HideProgressTextByThread()
	{
		m_MapActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				m_MapActivity.HideProgressText();

			}

		});
	}

	public static void UpdateProgressTextByThread()
	{
		SetProgressBarTextByThread(String.format(Constants.PROGRESS_TEXT, 
				m_iTotalFriendsMapped.intValue(), 
				m_iTotalFriendsCount.intValue(), 
				m_iCountFBFriendsUpdation, 
				m_iCountFBFriends));                  // mNotificationHelper.progressUpdate();

	}

	public static void EnableControlInBuddyListActivityByThread(final boolean bVisible)
	{
		if(m_BuddyListActivity != null)
		{
			m_BuddyListActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try
					{
						m_BuddyListActivity.SetVisibilityofControls(bVisible);
					}
					catch(Exception ex)
					{
						Utilities.PrintMessageInConsole(Log.getStackTraceString(ex));
					}

				}
			});
		}

	}
}
