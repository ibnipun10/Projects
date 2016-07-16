package com.UI.Common.library;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class CListItemListener implements OnItemClickListener 
{
	private List<FacebookFriend> m_lfbFriend;
	private Context m_context;

	public CListItemListener(List<FacebookFriend> lfbFriend, Context context)
	{
		m_lfbFriend = lfbFriend;
		m_context = context;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		String fbLink = m_lfbFriend.get(arg2).link;

		if(fbLink != null)
		{
			Uri uri = Uri.parse( fbLink );
			m_context.startActivity( new Intent( Intent.ACTION_VIEW, uri ) );
		}
	}

}

