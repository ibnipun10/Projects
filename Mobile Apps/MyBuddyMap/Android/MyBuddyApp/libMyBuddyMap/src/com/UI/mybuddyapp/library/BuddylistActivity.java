package com.UI.mybuddyapp.library;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import com.UI.Common.library.CGraphChart;
import com.UI.Common.library.CListItemListener;
import com.UI.Common.library.CommonFacebookClass;
import com.UI.Common.library.Constants;
import com.UI.Common.library.DBController;
import com.UI.Common.library.FacebookFriend;
import com.UI.Common.library.LocationClass;
import com.UI.Common.library.Utilities;
import com.UI.mybuddyapp.library.R;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import au.com.bytecode.opencsv.CSVWriter;

public class BuddylistActivity extends Activity {

	private ListView mlist;
	private ListAdaptor mListAdaptor;
	private String m_key;
	private TextView mtxtTitle;
	private TextView mtxtsubTitle;
	private Menu mMenu;	
	private boolean m_bHomeTown;
	private String m_myId;
	private String m_MyName;
	private List<FacebookFriend> m_AllFriends;
	private List<FacebookFriend> m_KeyFriends;
	private android.view.MenuItem m_RefreshMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buddylist);

		try
		{
			Bundle values = getIntent().getExtras();
			m_key = values.getString(Constants.DICTIONARY_KEY);
			m_bHomeTown = values.getBoolean(Constants.INTENT_HOMETOWN);
			m_myId = values.getString(Constants.INTENT_MYUSERID);
			m_MyName = values.getString(Constants.INTENT_MYNAME);

			// TODO Auto-generated method stub
			mlist = (ListView) findViewById(R.id.list);
			m_AllFriends = new ArrayList<FacebookFriend>();
			m_KeyFriends = new ArrayList<FacebookFriend>();

			CommonFacebookClass.m_BuddyListActivity = this;
			
			new ReadDatabaseClass().execute();

			Utilities.getOverflowMenu(this);
			Utilities.LoadAndCreateAd(this, R.id.adView);
		}
		catch(Exception ex)
		{
			Utilities.PrintMessageInConsole(Log.getStackTraceString(ex));
		}

	}
	
	@Override
	public void startActivity(Intent intent) {
	     
	     if(Intent.ACTION_SEARCH.equals(intent.getAction()))
	     {
		     intent.putExtra(Constants.DICTIONARY_KEY, m_key);
		     intent.putExtra(Constants.INTENT_HOMETOWN, m_bHomeTown);
		     intent.putExtra(Constants.INTENT_MYUSERID, m_myId);
	     }
				
	     super.startActivity(intent);
	 }
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		
		if(CommonFacebookClass.m_iStateofthreads != 1)
		{
			if(m_RefreshMenu != null)
				SetVisibilityofControls(false);
		}
		
		
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
	}

	
	private void InitializeCompomponents()  {

		try
		{
			
			if(CommonFacebookClass.m_iStateofthreads != 1)
			{
				if(m_RefreshMenu != null)
					SetVisibilityofControls(false);
			}

			mListAdaptor = new ListAdaptor(m_KeyFriends, this);
			mlist.setAdapter(mListAdaptor);

			if(Utilities.IsPaidApp(this))
				mlist.setOnItemClickListener(new CListItemListener(m_KeyFriends, this));

			String strTitle = String.format(Constants.BUDDYLIST_TITLE,
					m_KeyFriends.size());
			this.setTitle(strTitle);
			//mtxtTitle = (TextView) this.findViewById(R.id.idbuddyListTitle);
			mtxtsubTitle = (TextView) this.findViewById(R.id.idbuddyListLocation);

			//mtxtTitle.setText();

			mtxtsubTitle.setText(m_KeyFriends.get(0).location.name);
		}
		catch(Exception ex)
		{
			Utilities.PrintMessageInConsole(Log.getStackTraceString(ex));
		}

	}

	private class ReadDatabaseClass extends AsyncTask<Void, Void, Void> {
		ProgressDialog mprogressDialog;

		@Override
		protected void onPreExecute() {
			/*
			 * This is executed on UI thread before doInBackground(). It is
			 * the perfect place to show the progress dialog.
			 */
			mprogressDialog = Utilities.ShowProgressDialog(BuddylistActivity.this);
		}

		@Override
		protected Void doInBackground(Void... voids) {

			try {
				InitializeListoffriendsFromDB();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Void res) {
			InitializeCompomponents();
			Utilities.CancelProgressDialog(mprogressDialog);

		}

	}




	private String GetCSVFileName(boolean bExportAll)
	{
		String fileType = ".csv";
		String fileName = null;
		if(bExportAll)
		{
			fileName = m_MyName + fileType;
		}
		else
		{
			fileName = m_KeyFriends.get(0).location.name;
			fileName = fileName.replaceAll(" ", "");
			Time t = new Time(Time.getCurrentTimezone());
			t.setToNow();
			String date = t.format("%Y-%m-%d");

			fileName = fileName + "_" + date + fileType;

		}

		return fileName;
	}

	private List<FacebookFriend> GetCopyofFacebookFriendList(
			ArrayList<FacebookFriend> lfbFriend) { // TODO Auto-generated method
		// stub
		ArrayList<FacebookFriend> lFriend = new ArrayList<FacebookFriend>();
		synchronized (lfbFriend) {

			lFriend = (ArrayList<FacebookFriend>) lfbFriend.clone();
		}
		return lFriend;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buddylistactivity_menu, menu);
		mMenu = menu;

		PopulateMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}

	private void PopulateMenu(Menu menu)  {

		try
		{
			//refresh menu item
			int item = Utilities.GetIdentifier(this, "refreshbuddyList", "id");
			
			if(item != 0)
			{
				m_RefreshMenu = menu.findItem(item);
				m_RefreshMenu.setIcon(R.drawable.ic_menu_refresh);
			}
			else
			{
				Utilities.PrintMessageInConsole("Menu item not found");
			}
			
			int searchItem = Utilities.GetIdentifier(this, "menu_action_search", "id");
			if(searchItem != 0)
			{
				// Get the SearchView and set the searchable configuration
			    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			    SearchView searchView = (SearchView) menu.findItem(searchItem).getActionView();
			    
			    // Assumes current activity is the searchable activity
			    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
			    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default				
			}
			
				
		}
		catch (NoSuchFieldError e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utilities.PrintMessageInConsole(Log.getStackTraceString(e));
		}
	}

	public void SetVisibilityofControls(boolean bVisible)
	{

		if(m_RefreshMenu != null)
		{
			m_RefreshMenu.setVisible(bVisible);
			m_RefreshMenu.setEnabled(bVisible);
		}


	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		
		int itemId = item.getItemId();
		if (itemId == Utilities.GetIdentifier(this, "exportcsv", "id")) {
			SendListAsCSVInMail(false);
		}
		else if(itemId == Utilities.GetIdentifier(this, "exportAllcsv", "id")){
			SendListAsCSVInMail(true);
		}
		else if(itemId == Utilities.GetIdentifier(this, "refreshbuddyList", "id")){
			new ReadDatabaseClass().execute();
		}
		else if(itemId == Utilities.GetIdentifier(this, "menu_buy", "id")){
			Utilities.OnBuyButtonSelected(this);
		}
		else if(itemId == Utilities.GetIdentifier(this, "Test", "id"))
		{
			Intent intent = new Intent(this, GraphActivity.class);
			intent.putExtra(Constants.DICTIONARY_KEY, m_key);
			intent.putExtra(Constants.INTENT_HOMETOWN, m_bHomeTown);
			intent.putExtra(Constants.INTENT_MYUSERID, m_myId);
			intent.putExtra(Constants.INTENT_MYNAME, m_MyName);
			
			startActivity(intent);
		     
		}
		return true;
	}

	private String[] GetCSVValue(FacebookFriend friends, boolean bHeader) throws Exception
	{
		if (friends == null)
			throw new Exception("Friend list is NULL");

		Field[] fields = friends.getClass().getFields();
		List<String> header = new ArrayList<String>();

		for (int j = 0; j < fields.length; j++) 
		{
			Field field = fields[j];

			String fieldName = field.getName();
			if(fieldName.compareTo("id") == 0 || fieldName.compareTo("link") == 0 || fieldName.equals("myId"))
				continue;

			if(field.getType().equals(String.class))
			{			
				if(bHeader)
					header.add(fieldName);
				else
					header.add((String)field.get(friends));

			}
			else if(field.getType().equals(LocationClass.class))
			{
				LocationClass objLocation = (LocationClass)field.get(friends);
				Field[] Locfields = objLocation.getClass().getFields();

				for(int i=0; i< Locfields.length; i++)
				{
					Field locfield = Locfields[i];

					if(locfield.getName().equals("name"))
					{
						if(bHeader)
							header.add("Location");
						else
						{
							Object objValue = locfield.get(objLocation);
							if(objValue != null)
								header.add((String)objValue);
							else
								header.add("");
						}
					}
				}
			}
		}

		return header.toArray(new String[header.size()]);
	}

	private void InitializeListoffriendsFromDB() throws IllegalAccessException, InstantiationException
	{
		if(m_AllFriends != null)
			m_AllFriends.clear();
		else
			m_AllFriends = new ArrayList<FacebookFriend>();

		if(m_KeyFriends != null)
			m_KeyFriends.clear();
		else
			m_KeyFriends = new ArrayList<FacebookFriend>();

		DBController dbController = Utilities.getDbController(this, m_bHomeTown, m_myId);
		List<FacebookFriend> lDBfbFriend = dbController.ReadRowsInTable(null);


		for(int i =0; i<lDBfbFriend.size(); i++)
		{
			FacebookFriend friend = lDBfbFriend.get(i);
			if(friend != null)
			{
				if(friend.location.id != null)
				{

					m_AllFriends.add(friend);

					String strKey = Utilities.GetKey(friend.location.lattitude, friend.location.longitude);
					if(m_key.equals(strKey))
						m_KeyFriends.add(friend);

				}
			}


		}

	}

	private void SendListAsCSVInMail(boolean bExportAll) 
	{		
		try
		{
			GetExternalStoragePath();
			String csv =  getCacheDir().getAbsolutePath();

			csv = csv + File.separator + GetCSVFileName(bExportAll);

			CSVWriter writer;

			writer = new CSVWriter(new FileWriter(csv));

			List<String[]> data = new ArrayList<String[]>();
			List<FacebookFriend> lfbFriend;

			if(bExportAll)
				lfbFriend = m_AllFriends;
			else
				lfbFriend = m_KeyFriends;

			if(lfbFriend == null)
				return;

			data.add(GetCSVValue(lfbFriend.get(0), true));

			for(int i=0; i<lfbFriend.size(); i++)
			{
				data.add(GetCSVValue(lfbFriend.get(i), false));
			}			

			writer.writeAll(data);

			writer.close();		

			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain"); // send email as plain text
			intent.putExtra(Intent.EXTRA_SUBJECT, Constants.EMAIL_ATTACHMENT_SUBJECT);
			intent.putExtra(Intent.EXTRA_TEXT, "");

			File shareFile = new File(csv);
			shareFile.setReadable(true, false);

			Uri uri = Uri.fromFile(shareFile);
			intent.putExtra(Intent.EXTRA_STREAM, uri);

			startActivity(Intent.createChooser(intent, ""));
		}
		catch(Exception ex)
		{
			// ex.getMessage();
			ex.printStackTrace();
			Utilities.PrintMessageInConsole("NipunError" + ex.getMessage());
		}
	}

	public String GetExternalStoragePath() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) 
		{
			Log.i("MyBuddyMap", "External Storage is available");
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}

		Log.e("MyBuddyMap", "External Storage is not available");
		return null;
	}


}
