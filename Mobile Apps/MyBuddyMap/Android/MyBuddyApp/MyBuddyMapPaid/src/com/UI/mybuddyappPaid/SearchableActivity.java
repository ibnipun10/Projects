package com.UI.mybuddyappPaid;

import java.util.ArrayList;
import java.util.List;

import com.UI.Common.library.CListItemListener;
import com.UI.Common.library.Constants;
import com.UI.Common.library.DBController;
import com.UI.Common.library.FacebookFriend;
import com.UI.Common.library.Utilities;
import com.UI.mybuddyapp.library.ListAdaptor;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchableActivity extends Activity {

	private ListView mlist;
	private ListAdaptor mListAdaptor;
	private List<FacebookFriend> m_AllFriends;
	private boolean m_bHomeTown;
	private String m_myId;
	private String m_key;
	private List<FacebookFriend> m_KeyFriends;
	private TextView mtxtsubTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		Bundle values = getIntent().getExtras();

		m_key = values.getString(Constants.DICTIONARY_KEY);
		m_bHomeTown = values.getBoolean(Constants.INTENT_HOMETOWN);
		m_myId = values.getString(Constants.INTENT_MYUSERID);

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			// doMySearch(query);
			Utilities.PrintMessageInConsole("Search query : " + query);

			InitializeComponents(query);
		}
	}

	private void InitializeComponents(String query)
	{
		try
		{
			InitializeListoffriendsFromDB(query);
			mlist = (ListView) findViewById(R.id.list);
			mListAdaptor = new ListAdaptor(m_KeyFriends, this);
			mlist.setAdapter(mListAdaptor);


			mlist.setOnItemClickListener(new CListItemListener(m_KeyFriends, this));

			String strTitle = String.format(Constants.BUDDYLIST_TITLE,
					m_KeyFriends.size());
			this.setTitle(strTitle);

		}
		catch(Exception ex)
		{
			Utilities.PrintMessageInConsole(Log.getStackTraceString(ex));
		}

	}

	private void InitializeListoffriendsFromDB(String query) throws IllegalAccessException, InstantiationException
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
		List<FacebookFriend> lDBfbFriend = dbController.ReadRowswithNameQuery(null , query);


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


}
