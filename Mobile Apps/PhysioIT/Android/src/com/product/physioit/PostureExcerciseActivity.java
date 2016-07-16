package com.product.physioit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import Common.Constants;
import Common.PhysioITTables;
import Common.Utilities;
import Controller.DBController;

/**
 * Created by nipuna on 8/18/13.
 */
public class PostureExcerciseActivity extends Activity {
    private PostureExerciseAdaptor adaptorPostureExercise;
    private List<PhysioITTables.PostureDB> postureDBList;
    private List<PhysioITTables.ExcerciseDB> exerciseDBList;
    private DBController _dbController;
    ExpandableListView expandlistView;
    private int _remedyID;
    private int _bodyPartID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posture_exercise);

        _dbController = new DBController(new ContextWrapper(this.getApplicationContext()), Constants.PhysioITdatabase, Constants.DATABASE_VERSION_ID);

        expandlistView = (ExpandableListView)findViewById(R.id.expandableListView);

        Bundle values = getIntent().getExtras();
        _bodyPartID = values.getInt(Constants.COLUMN_PARTID);
        _remedyID = values.getInt(Constants.COLUMN_REMEDYID);

       // InitializeComponents();
        new ReadDatabaseClass().execute();
    }

    private class ReadDatabaseClass extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog mprogressDialog;

        @Override
        protected void onPreExecute()
        {
                /*
                 * This is executed on UI thread before doInBackground(). It is
                 * the perfect place to show the progress dialog.
                 */
            mprogressDialog =  Utilities.ShowProgressDialog(PostureExcerciseActivity.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                SetPostureDBList();
                SetExerciseDBList();
            } catch(Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids)
        {
            Utilities.CancelProgressDialog(mprogressDialog);
            InitializeComponents();

        }
    }

    private void InitializeComponents()
    {
        try
        {
            adaptorPostureExercise = new PostureExerciseAdaptor(this, postureDBList, exerciseDBList);
            expandlistView.setVerticalScrollBarEnabled(true);
            expandlistView.setAdapter(adaptorPostureExercise);
        }
        catch(Exception ex)
        {
            Log.i("PostureExerciseActivity", ex.getMessage());
        }
    }

    private void SetPostureDBList() throws InstantiationException, IllegalAccessException {
        Dictionary<String, Integer> dictionary = new Hashtable<String, Integer>();
        dictionary.put(Constants.COLUMN_PARTID, _bodyPartID);
        dictionary.put(Constants.COLUMN_REMEDYID, _remedyID);
        postureDBList = _dbController.ReadRowsInTable(new PhysioITTables.PostureDB(), dictionary);
    }

    private void SetExerciseDBList() throws InstantiationException, IllegalAccessException {
        Dictionary<String, Integer> dictionary = new Hashtable<String, Integer>();
        dictionary.put(Constants.COLUMN_PARTID, _bodyPartID);
        dictionary.put(Constants.COLUMN_REMEDYID, _remedyID);
        exerciseDBList = _dbController.ReadRowsInTable(new PhysioITTables.ExcerciseDB(), dictionary);
    }
}