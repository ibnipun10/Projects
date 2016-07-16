package com.UI.mybuddyapp.library;

import java.util.ArrayList;
import java.util.List;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;

import com.UI.Common.library.CLinkedinLogin;
import com.UI.Common.library.CommonFacebookClass;
import com.UI.Common.library.Constants;
import com.UI.Common.library.Utilities;
import com.UI.mybuddyapp.library.R;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class MainFragment extends Fragment {

	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	private GraphUser user;
	private View mView;
	private LinearLayout mLinearLayoutMain;
	private TextView mCentreText;
	private TextView m_TapOn;
	Button m_btnHometown;
	Button m_btnLocation;
	Session m_session;
	Button m_btnLinkedin;
	SocialAuthAdapter m_adaptor;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.activity_main, container, false);

		m_btnHometown = (Button) mView.findViewById(R.id.btnHometown);
		m_btnHometown.setOnClickListener(TapOnHomeTownButton);
		
		m_btnLinkedin = (Button)mView.findViewById(R.id.btnLinkedin);
		m_btnLinkedin.setText("Linkedin");
				
		m_btnLinkedin.setOnClickListener(new OnClickListener() 
	    {
	       public void onClick(View v) 
	       {
	    	  CLinkedinLogin objLinkedin = new CLinkedinLogin(mView.getContext(), getString(R.string.linkedin_consumer_key), getString(R.string.linkedin_consumer_secret));
	    	  objLinkedin.ShowDialog();
	       }
	   });


		m_btnLocation = (Button) mView.findViewById(R.id.btnLocation);
		m_btnLocation.setOnClickListener(TapOnLocationBUtton);

		m_TapOn = (TextView) mView.findViewById(R.id.idtxtTapOn);

		//HideControls(true);

		LoginButton authButton = (LoginButton) mView
				.findViewById(R.id.login_button);
		authButton.setFragment(this);
		authButton.setReadPermissions(SetPermission());

		InitializeComponents();
		authButton
		.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {

				if (user != null) {
					MainFragment.this.user = user;

					TextView welcomeText = (TextView) mView
							.findViewById(R.id.idMyName);
					welcomeText.setText(user.getName());

					ProfilePictureView myImg = (ProfilePictureView) mView
							.findViewById(R.id.ProfilePicture);
					myImg.setProfileId(user.getId());
					
					InitializeDbController(mView.getContext());
					
				//	SetBeforeLoginControlVIsiblity(false);
				//	HideControls(false);
				}
			}
		});


		return mView;
	}
	
	private final class ResponseListener implements DialogListener 
	{
	   public void onComplete(Bundle values) {
	    
	      //adapter.updateStatus(edit.getText().toString(), new MessageListener());                   
	   }

	@Override
	public void onBack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(SocialAuthError arg0) {
		// TODO Auto-generated method stub
		
	}
	};
	
	
/*
	private void HideControls(boolean bhide)
	{
		if(bhide)
		{
			m_btnHometown.setVisibility(View.INVISIBLE);
			m_btnLocation.setVisibility(View.INVISIBLE);
			m_TapOn.setVisibility(View.INVISIBLE);
		}
		else
		{
			m_btnHometown.setVisibility(View.VISIBLE);
			m_btnLocation.setVisibility(View.VISIBLE);
			m_TapOn.setVisibility(View.VISIBLE);			
		}
	}
*/
	private List<String> SetPermission() {
		// TODO Auto-generated method stub
		List<String> permission = new ArrayList();

		permission.add("friends_about_me");
		permission.add("friends_birthday");
		permission.add("friends_hometown");
		permission.add("friends_location");
		permission.add("friends_relationships");
		permission.add("friends_relationship_details");

		return permission;
	}

	private void InitializeComponents() {
		// TODO Auto-generated method stub
		mLinearLayoutMain = (LinearLayout) mView
				.findViewById(R.id.idLayoutMain);
		mCentreText = (TextView) mView.findViewById(R.id.idcentreText);
		mCentreText.setText(Constants.MAINPAGE_MESSAGE);

		SetBeforeLoginControlVIsiblity(true);
	}

	private void SetBeforeLoginControlVIsiblity(boolean bVisible) {
		if (bVisible) {
			mLinearLayoutMain.setVisibility(View.GONE);
			mCentreText.setVisibility(View.VISIBLE);
			
		} else {
			mLinearLayoutMain.setVisibility(View.VISIBLE);
			mCentreText.setVisibility(View.GONE);
		}
	}

	private View.OnClickListener TapOnHomeTownButton = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			try
			{
				Utilities.bKill = false;
				Intent intent = new Intent(view.getContext(), MapActivity.class);
				CreateBundleToPass(intent, true);			
				startActivity(intent);
			}
			catch(Exception ex)
			{
				Utilities.PrintMessageInConsole(Log.getStackTraceString(ex));
			}
		}
	};

	private View.OnClickListener TapOnLocationBUtton = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			
			try
			{
				Utilities.bKill = false;
				Intent intent = new Intent(view.getContext(), MapActivity.class);
				CreateBundleToPass(intent, false);
				startActivity(intent);
			}
			catch(Exception ex)
			{
				Utilities.PrintMessageInConsole(Log.getStackTraceString(ex));
			}
			
		}
	};

	private void CreateBundleToPass(Intent intent, boolean bHomeTown) throws Exception
	{
		intent.putExtra(Constants.INTENT_HOMETOWN, bHomeTown);
		
		if(user == null && user.getId() == null)
		{
			//Print a msg box
			throw new Exception("User id is null, Unable to connect to facebook");
		}
		intent.putExtra(Constants.INTENT_MYUSERID, user.getId());
		intent.putExtra(Constants.INTENT_MYNAME, user.getName());
	}
	
	private void InitializeDbController(Context context) {
		Utilities.getDbController(context, true, user.getId());
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		try {
			if (state.isOpened()) {
				if(!session.getPermissions().containsAll(SetPermission()))
					session.requestNewReadPermissions(new NewPermissionsRequest(MainFragment.this, SetPermission()));
				CommonFacebookClass.session = session;
				m_session = session;
				SetBeforeLoginControlVIsiblity(false);
				//HideControls(false);

			} else if (state.isClosed()) {
				Log.i(TAG, "Logged out...");
				//HideControls(true);
				SetBeforeLoginControlVIsiblity(true);			

			} 

		} catch (Exception ex) {
			Utilities.PrintMessageInConsole("NipunError" + ex.getMessage());
		}
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onResume() {
		super.onResume();

		// For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
		
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
	
	

}
