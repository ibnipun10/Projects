package com.UI.mybuddyapp.library;

import com.UI.Common.library.CommonFacebookClass;
import com.UI.Common.library.Constants;
import com.UI.Common.library.FacebookFriend;
import com.UI.Common.library.NotificationHelper;
import com.UI.Common.library.Utilities;

public class PopulateLocationThread extends Thread {

	private NotificationHelper mNotificationHelper;
	private FacebookFriend fbFriend;
	private boolean m_bHomeTown;

	public PopulateLocationThread(NotificationHelper notificationHelper, boolean bHomeTown) {
		// TODO Auto-generated constructor stub
		mNotificationHelper = notificationHelper;
		m_bHomeTown = bHomeTown;

	}
	public void run()
	{
		try
		{
			Utilities.PrintMessageInConsole("Starting PopulateLocationThread");

			while (!CommonFacebookClass.bDone)
			{
				if(Utilities.bKill)
					throw new Exception("Killing Location thread");
				
				if(CommonFacebookClass.queueFriends == null)
				{
					Thread.sleep(1000);
					continue;
				}

				if (CommonFacebookClass.queueFriends.size() != 0)
				{
					fbFriend = CommonFacebookClass.RemoveItemInQueue();

					if(fbFriend == null)
						continue;

					if (fbFriend.location != null && fbFriend.location.id != null)
					{

						CommonFacebookClass.ShowLocationOnMapByThread(fbFriend);
						CommonFacebookClass.IncrementFriendsMapped(true);						
						
						// ShowLocationByThread(fbFriend.location.lattitude, fbFriend.location.longitude);

						CommonFacebookClass.UpdateProgressTextByThread();
					}

				}
				else
				{
					Thread.sleep(100);
				}
			}

			//SetProgressIndicatorVisibility(false);
		}
		catch (Exception ex)
		{
			//Set Error Text
			Utilities.PrintMessageInConsole(ex.getMessage());
			Utilities.PrintMessageInConsole(ex.getStackTrace().toString());

		}
		finally
		{
			Utilities.PrintMessageInConsole("Completed Thread");
			Utilities.bKill = true;
			//ShowMappedFriendsFinalMessage();
			//mNotificationHelper.completed();
			CommonFacebookClass.EnableControlInBuddyListActivityByThread(false);
			CommonFacebookClass.HideProgressTextByThread();
			CommonFacebookClass.m_iStateofthreads = 2;
		}

		
	}

	private void ShowMappedFriendsFinalMessage()
	{	
		int TotalFriendsCount = CommonFacebookClass.m_iTotalFriendsCount.intValue();
		int TotalFriendsMapped = CommonFacebookClass.m_iTotalFriendsMapped.intValue();
		
		
		CommonFacebookClass.SetProgressBarTextByThread(String.format(Constants.FINAL_MAPPED_MESSAGE, TotalFriendsMapped));                  // mNotificationHelper.progressUpdate();
		
	}
		
}
