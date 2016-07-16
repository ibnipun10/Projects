package com.UI.mybuddyapp.library;

import java.util.List;

import com.UI.Common.library.FacebookFriend;
import com.UI.mybuddyapp.library.R;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.ProfilePictureView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdaptor extends BaseAdapter {

	private List<FacebookFriend> mFBFriend;
	private Activity mActivity;
	private LayoutInflater mInflater=null;

	public ListAdaptor(List<FacebookFriend> lfbFriend, Activity activity) {
		// TODO Auto-generated constructor stub
		mFBFriend = lfbFriend;
		mActivity = activity;
		mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFBFriend.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 View vi=convertView;
	        if(convertView==null)
	            vi = mInflater.inflate(R.layout.adaptor_listview, null);
	 
	        TextView txtName = (TextView)vi.findViewById(R.id.idName); 
	        TextView txtBirthday = (TextView)vi.findViewById(R.id.idBirthday); 
	        TextView txtStatus = (TextView)vi.findViewById(R.id.idStatus); 
	        TextView txtSex =(TextView)vi.findViewById(R.id.idSex); 
	        ProfilePictureView picProfile = (ProfilePictureView)vi.findViewById(R.id.idPicture);
	 
	        
	        FacebookFriend fbfriend = mFBFriend.get(position);
	 
	        // Setting all values in listview
	        if(fbfriend.name != null)
	        	txtName.setText(fbfriend.name);
	        
	        if(fbfriend.birthday != null)
	        	txtBirthday.setText(fbfriend.birthday);
	        
	        if(fbfriend.relationship_status != null)
	        	txtStatus.setText(fbfriend.relationship_status);
	        
	        if(fbfriend.gender != null)
	        	txtSex.setText(fbfriend.gender);
	        
	        picProfile.setProfileId(String.valueOf(fbfriend.id));

	        return vi;
	}
}
