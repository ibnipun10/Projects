package com.UI.mybuddyapp.library;

import java.util.List;

import org.achartengine.GraphicalView;

import com.UI.Common.library.CGraphChart;
import com.UI.Common.library.Constants;
import com.UI.Common.library.DBController;
import com.UI.Common.library.FacebookFriend;
import com.UI.Common.library.Utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GraphActivity extends Activity {
	
	private boolean m_bHomeTown;
	private String m_myId;
	private String m_MyName;
	private String m_key;
	private Context m_context;
	private List<FacebookFriend> m_lfbFriend;
	public static Toast m_toast;
	public static int m_iLocationCount;
	
	public static void showToast(Context context, String text, int duration, boolean bShow)
	{
		if(m_toast != null)
			m_toast.cancel();
		
		if(bShow)
		{
			m_toast = Toast.makeText(context, text, duration);
			m_toast.show();
		}
	}
	
	@Override
	protected void onStop()
	{
		showToast(this, "", 0, false);
		super.onStop();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		
		Bundle values = getIntent().getExtras();
		
		m_key = values.getString(Constants.DICTIONARY_KEY);
		m_bHomeTown = values.getBoolean(Constants.INTENT_HOMETOWN);
		m_myId = values.getString(Constants.INTENT_MYUSERID);
		m_MyName = values.getString(Constants.INTENT_MYNAME);
		m_context = this;
		
		new ReadDatabaseClass().execute();
		
		
	}
	
	
	private class ReadDatabaseClass extends AsyncTask<Void, Void, List<FacebookFriend>> {
		ProgressDialog mprogressDialog;

		@Override
		protected void onPreExecute() {
			/*
			 * This is executed on UI thread before doInBackground(). It is
			 * the perfect place to show the progress dialog.
			 */
			mprogressDialog = Utilities.ShowProgressDialog(GraphActivity.this);
		}

		@Override
		protected List<FacebookFriend> doInBackground(Void... voids) {

			List<FacebookFriend> lfbFriend  = null;
			try {
					DBController dbController = Utilities.getDbController(m_context, m_bHomeTown, m_myId);
					lfbFriend = dbController.ReadRowsInTable(null);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return lfbFriend;

		}

		@Override
		protected void onPostExecute(List<FacebookFriend> res) {
			
			CGraphChart objGraphChart = new CGraphChart(res, m_context);
			GraphicalView gView = objGraphChart.getPieChartView();
			
			LinearLayout chartLayout = (LinearLayout)findViewById(R.id.chart_layout);
			chartLayout.addView(gView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			
			String title = String.format(Constants.GRAPH_ACTIVTTY_TITLE, m_iLocationCount); 
			setTitle(title);
			
			Utilities.CancelProgressDialog(mprogressDialog);

		}

	}

}
