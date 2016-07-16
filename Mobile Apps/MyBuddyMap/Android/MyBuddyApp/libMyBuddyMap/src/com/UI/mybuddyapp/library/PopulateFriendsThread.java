package com.UI.mybuddyapp.library;

import java.io.Console;

import android.util.Log;

import com.UI.Common.library.CommonFacebookClass;
import com.UI.Common.library.Constants;
import com.UI.Common.library.FacebookFriend;
import com.UI.Common.library.Utilities;

public class PopulateFriendsThread extends Thread {
	
	private boolean m_bHomeTown;
	
	PopulateFriendsThread(boolean bhometown)
	{
		m_bHomeTown = bhometown;
	}

	public void run()
	{
		try {
				CommonFacebookClass.SetProgressBarTextByThread(Constants.DB_READING_DB);
				Utilities.PrintMessageInConsole("Starting PopulateFriendsThread");
			
				//Get all friends from db and mark them on map
				CommonFacebookClass.GetAllFriendsFromDB();
				
				
			
				//Get all friends from facebook and then either update them or do nothing
				CommonFacebookClass.SetProgressBarTextByThread(Constants.FB_READING_FB);
				CommonFacebookClass.GetAllFriends();
				Utilities.PrintMessageInConsole("Total friends from Facebook: " + CommonFacebookClass.lfriends.size());
			//	CommonFacebookClass.SetProgressBarTextByThread(String.format(Constants.FB_FRIEDNS_RECEIVED, CommonFacebookClass.lfriends.size()));
				
				int iCount = 0;
				CommonFacebookClass.m_iCountFBFriends = CommonFacebookClass.lfriends.size();
	
				for (String id : CommonFacebookClass.lfriends) 
				{
					if(Utilities.bKill)
						return;
					CommonFacebookClass.m_iCountFBFriendsUpdation++;
					
				    CommonFacebookClass.UpdateListofFriendsTobeDeletedFromDB(id);
					
					FacebookFriend fbfriend =  CommonFacebookClass.GetFriendsDetails(id);
	
				if(fbfriend != null)
				{
					fbfriend.location = CommonFacebookClass.GetLocationDetails(fbfriend.location);
					
					CommonFacebookClass.InsertIntoDBDictionary(fbfriend, false);
				}
			}

			CommonFacebookClass.bDone = true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utilities.PrintMessageInConsole("PopulateFriendsThread"
					+ "" + e.getMessage());
			Utilities.PrintMessageInConsole(Log.getStackTraceString(e));
			Utilities.bKill = true;
		}

	}

}

