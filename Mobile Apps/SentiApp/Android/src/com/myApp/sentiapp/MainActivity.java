package com.myApp.sentiapp;

import com.facebook.model.GraphUser;
import com.facebook.samples.hellofacebook.HelloFacebookSampleActivity;
import com.facebook.samples.hellofacebook.R;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private LoginButton loginButton;
	private GraphUser user;
	private ProfilePictureView profilePictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
            	MainActivity.this.user = user;
                updateUI();
            }
        });

        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
